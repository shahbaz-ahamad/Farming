package com.shahbaz.farming.di

import android.content.Context
import com.shahbaz.farming.oboarding.onboardingrepo.OnBoardingRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FarmingModule {

    @Provides
    @Singleton
    fun provideOnboardingRepo(@ApplicationContext context: Context):OnBoardingRepo{
        return OnBoardingRepo(context)
    }
}