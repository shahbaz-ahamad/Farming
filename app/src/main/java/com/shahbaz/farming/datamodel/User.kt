package com.shahbaz.farming.datamodel

data class User(
    val id:String,
    val name:String,
    val email:String,
    val profileUrl:String?=null
)
