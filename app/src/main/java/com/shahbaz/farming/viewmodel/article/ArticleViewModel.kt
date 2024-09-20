package com.shahbaz.farming.viewmodel.article

import androidx.lifecycle.ViewModel
import com.shahbaz.farming.repo.article.ArticleRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val articleRepository: ArticleRepo,
) : ViewModel() {
    val article_state = articleRepository.articleState

    fun getArticle(name: String) {
        articleRepository.getArticle(name)
    }
}