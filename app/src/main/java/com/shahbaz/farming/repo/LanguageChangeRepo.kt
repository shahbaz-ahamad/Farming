package com.shahbaz.farming.repo

import android.content.Context
import android.content.SharedPreferences

class LanguageChangeRepo {
    fun saveSelectedLanguage(context: Context, language: String) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("LanguagePref", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("selected_language", language)
        editor.apply() // Save changes asynchronously
    }

    fun getSelectedLanguage(context: Context): String? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("LanguagePref", Context.MODE_PRIVATE)
        return sharedPreferences.getString("selected_language", "English") // Default to "English"
    }

}