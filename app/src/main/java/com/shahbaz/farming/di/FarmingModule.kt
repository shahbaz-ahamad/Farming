package com.shahbaz.farming.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.shahbaz.farming.authentication.AuthenticationRepositiory
import com.shahbaz.farming.oboarding.onboardingrepo.OnBoardingRepo
import com.shahbaz.farming.repo.AddPostRepo
import com.shahbaz.farming.repo.AddProductRepo
import com.shahbaz.farming.repo.CartRepo
import com.shahbaz.farming.repo.Detailsfragmentrepo
import com.shahbaz.farming.repo.HomeFragmentRepositiory
import com.shahbaz.farming.retrofit.WeatherApi
import com.shahbaz.farming.util.Constant.Companion.WEATHER_BASE_URL
import com.shahbaz.farming.weather.WeatherRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FarmingModule {

    @Provides
    @Singleton
    fun provideOnboardingRepo(@ApplicationContext context: Context): OnBoardingRepo {
        return OnBoardingRepo(context)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthenticationViewmodel(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): AuthenticationRepositiory {
        return AuthenticationRepositiory(firebaseAuth, firestore)
    }


    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(WEATHER_BASE_URL) // Replace with your base URL
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @Provides
    @Singleton
    fun provideWeatherApi(retrofit: Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherRepo(weatherApi: WeatherApi): WeatherRepo {
        return WeatherRepo(weatherApi)
    }

    @Provides
    @Singleton
    fun provideHomeRepo(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
        firebaseStorage: FirebaseStorage
    ): HomeFragmentRepositiory {
        return HomeFragmentRepositiory(firebaseAuth, firestore, firebaseStorage)
    }

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Provides
    @Singleton
    fun provideAddPostRepo(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
        firebaseStorage: FirebaseStorage
    ): AddPostRepo {
        return AddPostRepo(firebaseAuth, firebaseStorage, firestore)
    }

    @Provides
    @Singleton
    fun provideAddProductRepo(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
        firebaseStorage: FirebaseStorage
    ): AddProductRepo {
        return AddProductRepo(firebaseAuth, firestore, firebaseStorage)
    }

    @Provides
    @Singleton
    fun provideDetailsFragmentrepo(firestore: FirebaseFirestore,firebaseAuth: FirebaseAuth): Detailsfragmentrepo {
        return Detailsfragmentrepo(firestore,firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideCartRepo(firestore: FirebaseFirestore,firebaseAuth: FirebaseAuth): CartRepo {
        return CartRepo(firestore,firebaseAuth)
    }
}