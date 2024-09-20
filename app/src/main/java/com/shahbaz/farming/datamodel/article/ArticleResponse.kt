package com.shahbaz.farming.datamodel.article

data class ArticleResponse(
    val data: List<Data>,
    val included: List<Included>
)