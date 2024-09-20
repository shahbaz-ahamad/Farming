package com.shahbaz.farming.util

import android.content.Context
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.shahbaz.farming.repo.LanguageChangeRepo
import kotlinx.coroutines.tasks.await

class LanguageTranslator(private val context: Context) {

    private var translator: Translator? = null

    init {
        // Check if Hindi is selected and set up translation options
        val selectedLanguage = LanguageChangeRepo().getSelectedLanguage(context)
        if (selectedLanguage == "Hindi") {
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.HINDI)
                .build()
            translator = Translation.getClient(options)
        }
    }

    // Function to translate a string
    suspend fun translateText(text: String): String {
        return if (translator != null) {
            try {
                translator?.downloadModelIfNeeded()?.await() // Ensure the translation model is downloaded
                translator?.translate(text)?.await() ?: text
            } catch (e: Exception) {
                text // Return original text in case of failure
            }
        } else {
            text // No translation if Hindi is not selected
        }
    }

    // Clean up the translator when done
    fun closeTranslator() {
        translator?.close()
    }
}
