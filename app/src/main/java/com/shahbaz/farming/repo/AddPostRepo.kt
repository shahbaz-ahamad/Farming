package com.shahbaz.farming.repo

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.shahbaz.farming.datamodel.Post
import com.shahbaz.farming.util.Resources
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddPostRepo(
    val firebaseAuth: FirebaseAuth,
    val firebaseStorage: FirebaseStorage,
    val firestore: FirebaseFirestore
) {

    private val _postStauts = MutableStateFlow<Resources<Post>>(Resources.Unspecified())
    val postStatus = _postStauts.asStateFlow()

    private val _fetchPostStatus = MutableStateFlow<Resources<List<Post>    >>(Resources.Unspecified())
    val fetchPost = _fetchPostStatus.asStateFlow()

    fun createPost(imageUrl: String, title: String, description: String) {

        val uid = firebaseAuth.currentUser!!.uid
        _postStauts.value = Resources.Loading()
        //file name with file name and current user id
        val fileName = uid + imageUrl
        val storageReference = firebaseStorage.reference
        storageReference.child("PostImage").child(fileName)
            .putFile(Uri.parse(imageUrl))
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl
                    .addOnSuccessListener { url ->
                        val imageUrl = url.toString()
                        val post = Post(
                            id = uid+System.currentTimeMillis().toString(),
                            userName = firebaseAuth.currentUser!!.displayName.toString(),
                            userProfile = firebaseAuth.currentUser!!.photoUrl.toString(),
                            timeStamp = System.currentTimeMillis(),
                            title = title,
                            description = description,
                            image = imageUrl
                        )

                        firestore.collection("Post")
                            .document()
                            .set(post)
                            .addOnSuccessListener {
                                _postStauts.value = Resources.Success(post)
                            }
                            .addOnFailureListener { error ->
                                _postStauts.value = Resources.Error(error.localizedMessage)
                            }


                    }
                    .addOnFailureListener { e ->
                        _postStauts.value = Resources.Error(e.localizedMessage)
                    }

            }
            .addOnFailureListener {

            }
    }


    fun resetPostStatus() {
        _postStauts.value = Resources.Unspecified()
    }

    fun fetchPosts() {
        _fetchPostStatus.value = Resources.Loading()
        val uid = firebaseAuth.currentUser!!.uid
        firestore.collection("Post")
            .orderBy("timeStamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                _fetchPostStatus.value = Resources.Success(it.toObjects(Post::class.java))
            }
            .addOnFailureListener {
                _fetchPostStatus.value = Resources.Error(it.localizedMessage)
            }

    }
}