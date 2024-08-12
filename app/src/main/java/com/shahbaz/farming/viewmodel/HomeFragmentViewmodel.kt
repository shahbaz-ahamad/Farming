package com.shahbaz.farming.viewmodel

import androidx.lifecycle.ViewModel
import com.shahbaz.farming.repo.HomeFragmentRepositiory
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewmodel @Inject constructor(
    private val homeFragmentRepositiory: HomeFragmentRepositiory
):ViewModel() {

    val userDetailState = homeFragmentRepositiory.userDetailsStatus

    fun getCurrentUserDetails(){
        homeFragmentRepositiory.getCurrentUserDetail()
    }

    fun signOut(){
        homeFragmentRepositiory.signOut()
    }
}