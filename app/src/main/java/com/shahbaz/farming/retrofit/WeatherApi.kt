package com.shahbaz.farming.retrofit

import com.shahbaz.farming.datamodel.weahterdatamodel.WeatherRootList
import com.shahbaz.farming.util.Constant.Companion.WAETHER_API_KEY
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/weather")
    fun getWeatherData(
        @Query("lat") latitude:String,
        @Query("lon") longitude:String,
        @Query("appid") apiKey:String = WAETHER_API_KEY
    ): Call<WeatherRootList>
}