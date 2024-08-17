package com.shahbaz.farming.weather

import com.shahbaz.farming.datamodel.weahterdatamodel.WeatherRootList
import com.shahbaz.farming.retrofit.WeatherApi
import com.shahbaz.farming.util.Resources
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherRepo(
    val weatherApi: WeatherApi
) {

    private val _weatherState = MutableStateFlow<Resources<WeatherRootList>>(Resources.Unspecified())
    val weatherState = _weatherState.asStateFlow()


    fun getWeather(lat:String,lon:String){
        _weatherState.value= Resources.Loading()
        weatherApi.getWeatherData(lat,lon)
            .enqueue(object : Callback<WeatherRootList>{
                override fun onResponse(
                    call: Call<WeatherRootList>,
                    response: Response<WeatherRootList>
                ) {
                    if(response.isSuccessful){
                        val data = response.body()
                        if(data != null){
                            _weatherState.value= Resources.Success(data)
                        }
                    }
                }
                override fun onFailure(call: Call<WeatherRootList>, t: Throwable) {
                   _weatherState.value= Resources.Error(t.localizedMessage)
                }
            })
    }
}