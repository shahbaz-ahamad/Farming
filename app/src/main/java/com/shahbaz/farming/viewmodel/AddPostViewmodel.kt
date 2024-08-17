package com.shahbaz.farming.viewmodel

import androidx.lifecycle.ViewModel
import com.shahbaz.farming.repo.AddPostRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AddPostViewmodel @Inject constructor(
   private val addPostRepo: AddPostRepo
) :ViewModel(){

    val postStatus =  addPostRepo.postStatus
    val fetchPost = addPostRepo.fetchPost

    fun addPost(title:String,description:String,imageUrl:String){
        addPostRepo.createPost(imageUrl,title,description)
    }

    fun resetPostStatus() {
        addPostRepo.resetPostStatus()
    }

    fun fetchPost(){
        addPostRepo.fetchPosts()
    }
}