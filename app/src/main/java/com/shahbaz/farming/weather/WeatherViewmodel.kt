package com.shahbaz.farming.weather

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeatherViewmodel @Inject constructor(
    private val weatherRepo: WeatherRepo
) : ViewModel() {

    val weatherState = weatherRepo.weatherState

    fun getWeather(lat: String, lon: String) {
        weatherRepo.getWeather(lat, lon)
    }
}