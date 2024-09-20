package com.shahbaz.farming.retrofit

import com.shahbaz.farming.datamodel.article.ArticleResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleApi {

    @GET("crops")
    fun getCrops(
        @Query("filter") filter: String,
    ): Call<ArticleResponse>
}