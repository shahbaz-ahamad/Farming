package com.shahbaz.farming.oboarding.onboardingviewmodel

import androidx.lifecycle.ViewModel
import com.shahbaz.farming.oboarding.onboardingrepo.OnBoardingRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewmodel @Inject constructor(
    private val onBoardingRepo: OnBoardingRepo
) :ViewModel(){

    fun setOnBoardingCompleted(isCompleted :Boolean){
        onBoardingRepo.setOnBoardingCompleted(isCompleted)
    }

    fun isOnboardingCompleted():Boolean{
        return onBoardingRepo.isOnboardingCompleted()
    }
}