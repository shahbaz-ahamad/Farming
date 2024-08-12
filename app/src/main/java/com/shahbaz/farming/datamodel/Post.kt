package com.shahbaz.farming.datamodel

data class Post(
    val id: String,
    val userName:String,
    val userprofile:String,
    val timeStamp: Long,
    val title:String,
    val description:String,
    val image:String
)
