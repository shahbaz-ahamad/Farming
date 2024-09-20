package com.shahbaz.farming.repo

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.shahbaz.farming.datamodel.User
import com.shahbaz.farming.util.Resources
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeFragmentRepositiory(
    val firebaseAuth: FirebaseAuth,
    val firestore: FirebaseFirestore,
    val firebaseStorage: FirebaseStorage,
) {

    private val _userDetailsStatus = MutableStateFlow<Resources<User>>(Resources.Unspecified())
    val userDetailsStatus = _userDetailsStatus.asStateFlow()

    private val _updateProfilePicture = MutableStateFlow<Resources<String>>(Resources.Unspecified())
    val updateProfile = _updateProfilePicture.asStateFlow()

    private val _updateCoverPicture = MutableStateFlow<Resources<String>>(Resources.Unspecified())
    val updateCoverPicture = _updateCoverPicture.asStateFlow()

    fun signOut() {
        firebaseAuth.signOut()
    }

    fun getCurrentUserDetail() {
        _userDetailsStatus.value = Resources.Loading()
        firestore.collection("FarmerUser").document(firebaseAuth.currentUser!!.uid).get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                user?.let { user ->
                    _userDetailsStatus.value = Resources.Success(user)
                }
            }
            .addOnFailureListener {
                _userDetailsStatus.value = Resources.Error(it.localizedMessage)
            }
    }

    fun updateProfilePicture(selectedImageUrl: String) {
        _updateProfilePicture.value = Resources.Loading()
        val filename = firebaseAuth.currentUser?.uid
        val firebaseReference = firebaseStorage.reference
        firebaseReference.child("FarmerProfile")
            .child(filename.toString())
            .putFile(Uri.parse(selectedImageUrl))
            .addOnSuccessListener { task ->

                task.storage.downloadUrl.addOnSuccessListener { imageUri ->
                    val imageUrl = imageUri.toString()
                    firestore.collection("FarmerUser").document(firebaseAuth.currentUser!!.uid)
                        .update("profileUrl", imageUrl)
                        .addOnSuccessListener {
                            val profileUpdate = UserProfileChangeRequest
                                .Builder()
                                .setPhotoUri(Uri.parse(imageUrl))
                                .build()
                            firebaseAuth.currentUser!!.updateProfile(profileUpdate)
                                .addOnSuccessListener {
                                    _updateProfilePicture.value = Resources.Success(imageUrl)

                                }.addOnFailureListener { error ->
                                    _updateProfilePicture.value =
                                        Resources.Error(error.localizedMessage)
                                }

                        }
                        .addOnFailureListener { ex ->

                            _updateProfilePicture.value = Resources.Error(ex.localizedMessage)

                        }


                }
                    .addOnFailureListener { e ->
                        _updateProfilePicture.value = Resources.Error(e.localizedMessage)
                    }
            }
            .addOnFailureListener { exception ->
                _updateProfilePicture.value = Resources.Error(exception.localizedMessage)
            }
    }

    fun uploadCoverPhoto(coverImageUrl: String) {
        _updateCoverPicture.value = Resources.Loading()

        val firebaseReference = firebaseStorage.reference
        firebaseReference.child("FarmerProfile")
            .child(coverImageUrl)
            .putFile(Uri.parse(coverImageUrl))
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { imageUri ->
                    val imageUrl = imageUri.toString()
                    firestore.collection("FarmerUser").document(firebaseAuth.currentUser!!.uid)
                        .update("coverPhotoUrl", imageUrl)
                        .addOnSuccessListener {
                            _updateCoverPicture.value = Resources.Success(imageUrl)
                        }
                        .addOnFailureListener { ex ->
                            _updateCoverPicture.value = Resources.Error(ex.localizedMessage)
                        }
                }
                    .addOnFailureListener { e ->
                        _updateCoverPicture.value = Resources.Error(e.localizedMessage)
                    }
            }
            .addOnFailureListener { exception ->
                _updateCoverPicture.value = Resources.Error(exception.localizedMessage)
            }
    }

}