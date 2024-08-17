package com.shahbaz.farming.authentication

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewmodel @Inject constructor(
    private val authenticationRepositiory: AuthenticationRepositiory
):ViewModel() {
    val authenticationState = authenticationRepositiory.authenticationState

    val loginState = authenticationRepositiory.loginState

    fun createUser(email:String,password:String,name:String){
        authenticationRepositiory.createUser(email,password,name)
    }

    fun signInWithEmailAndPassword(email: String,password: String){
        authenticationRepositiory.signInWithEmailAndPassword(email,password)
    }

    fun  isLoggedIn():Boolean{
       return authenticationRepositiory.isLoggedIn()
    }
}