package com.shahbaz.farming.repo.article

import android.util.Log
import com.shahbaz.farming.datamodel.article.ArticleResponse
import com.shahbaz.farming.retrofit.ArticleApi
import com.shahbaz.farming.util.Resources
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleRepo(
    private val articleApi: ArticleApi,
) {

    private val _articleState =
        MutableStateFlow<Resources<ArticleResponse>>(Resources.Unspecified())
    val articleState = _articleState.asStateFlow()

    fun getArticle(name: String) {
        _articleState.value = Resources.Loading()
        articleApi.getCrops(name).enqueue(object : Callback<ArticleResponse> {
            override fun onResponse(
                call: Call<ArticleResponse>,
                response: Response<ArticleResponse>,
            ) {
                if (response.isSuccessful) {
                    Log.d("data", response.body().toString())
                    _articleState.value = Resources.Success(response.body()!!)
                } else {
                    _articleState.value = Resources.Error(response.message())
                }
            }

            override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                _articleState.value = Resources.Error(t.message.toString())
            }

        })
    }
}