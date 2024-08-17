package com.shahbaz.farming.oboarding.onboardingrepo

import android.content.Context
import android.content.SharedPreferences
import com.shahbaz.farming.util.Constant.Companion.KEY_ONBOARDING_COMPLETED
import com.shahbaz.farming.util.Constant.Companion.ONBOARDING_PREFERENCES

class OnBoardingRepo(
   private val context: Context
) {

    private val sharedPreferences:SharedPreferences=
        context.getSharedPreferences(ONBOARDING_PREFERENCES,Context.MODE_PRIVATE)


    fun setOnBoardingCompleted(isCompleted :Boolean){
        sharedPreferences.edit().putBoolean(KEY_ONBOARDING_COMPLETED,isCompleted).apply()
    }

    fun isOnboardingCompleted():Boolean{
        return sharedPreferences.getBoolean(KEY_ONBOARDING_COMPLETED,false)
    }
}