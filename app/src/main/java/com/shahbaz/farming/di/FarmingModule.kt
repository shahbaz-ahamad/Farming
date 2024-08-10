package com.shahbaz.farming.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.shahbaz.farming.authentication.AuthenticationRepositiory
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

    @Provides
    @Singleton
    fun provideFirebaseAuth():FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore():FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthenticationViewmodel(firebaseAuth: FirebaseAuth,firestore: FirebaseFirestore):AuthenticationRepositiory{
        return AuthenticationRepositiory(firebaseAuth,firestore)
    }
}