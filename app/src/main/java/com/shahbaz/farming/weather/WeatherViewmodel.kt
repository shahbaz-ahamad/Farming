package com.shahbaz.farming.weather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeatherViewmodel @Inject constructor(
    private val weatherRepo: WeatherRepo
) : ViewModel() {

    val weatherState = weatherRepo.weatherState


    private val _cityName = MutableLiveData<String>()
    val cityName: LiveData<String> get() = _cityName

    fun getWeather(lat: String, lon: String) {
        weatherRepo.getWeather(lat, lon)
    }

    fun setCityName(name: String) {
        Log.d("WeatherViewmodel", "Setting cityName to: $name")
        _cityName.value = name
    }
}