package com.shahbaz.farming.authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.shahbaz.farming.datamodel.User
import com.shahbaz.farming.util.Resources
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthenticationRepositiory(
    val firebaseAuth: FirebaseAuth,
    val firebaseFirestore: FirebaseFirestore
) {

    private var _authenticationState = MutableStateFlow<Resources<String>>(Resources.Unspecified())
    val authenticationState = _authenticationState.asStateFlow()

    private var _loginState = MutableStateFlow<Resources<String>>(Resources.Unspecified())
    val loginState = _loginState.asStateFlow()

    fun createUser(email: String, password: String, name: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                firebaseAuth.currentUser?.let { user1 ->
                    //change display name
                    val profileUpdate = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                    user1.updateProfile(profileUpdate)
                        .addOnSuccessListener {
                            //add user to firestore
                            val user = User(firebaseAuth.currentUser!!.uid, name, email, "","")
                            addUserToFirestore(user)
                        }
                        .addOnFailureListener { error ->
                            _authenticationState.value =Resources.Error(error.localizedMessage)
                        }

                }
                _authenticationState.value = Resources.Success("Success")
            }
            .addOnFailureListener {
                _authenticationState.value =
                    it.localizedMessage?.let { it1 -> Resources.Error(it1) }!!
            }
    }

    fun addUserToFirestore(user: User) {
        firebaseFirestore.collection("FarmerUser").document(user.id).set(user)
            .addOnSuccessListener {
            }
            .addOnFailureListener {
            }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        _loginState.value = Resources.Loading()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _loginState.value = Resources.Success("Success")
            }
            .addOnFailureListener {
                _loginState.value = Resources.Error(it.localizedMessage)
            }
    }

    fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }
}