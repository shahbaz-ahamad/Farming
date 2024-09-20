package com.shahbaz.farming.datamodel

data class FAQ(
    val id: Int,
    val question: String,
    val answer: String,
    var isExpanded: Boolean = false
)
