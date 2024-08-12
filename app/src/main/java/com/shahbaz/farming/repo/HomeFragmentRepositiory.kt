package com.shahbaz.farming.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.shahbaz.farming.datamodel.User
import com.shahbaz.farming.util.Resources
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeFragmentRepositiory(
    val firebaseAuth: FirebaseAuth,
    val firestore: FirebaseFirestore
) {

    private val _userDetailsStatus = MutableStateFlow<Resources<User>>(Resources.Unspecified())
    val userDetailsStatus = _userDetailsStatus.asStateFlow()


    fun signOut() {
        firebaseAuth.signOut()
    }

    fun getCurrentUserDetail(){
        _userDetailsStatus.value= Resources.Loading()
        firestore.collection("FarmerUser").document(firebaseAuth.currentUser!!.uid).get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                user?.let {user ->
                    _userDetailsStatus.value = Resources.Success(user)
                }
            }
            .addOnFailureListener {
                _userDetailsStatus.value=Resources.Error(it.localizedMessage)
            }
    }

}