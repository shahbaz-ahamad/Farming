package com.shahbaz.farming.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.shahbaz.farming.repo.LanguageChangeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class languageSelectionViewmodel @Inject constructor(
    private val languageChangeRepo: LanguageChangeRepo,
) : ViewModel() {

    fun saveSelectedLanguage(context: Context, language: String) {
        languageChangeRepo.saveSelectedLanguage(context, language)
    }

    fun getSelectedLanguage(context: Context): String {
        return languageChangeRepo.getSelectedLanguage(context)!!
    }
}