package com.shahbaz.farming.retrofit

import com.shahbaz.farming.datamodel.apmc.APMCMain
import com.shahbaz.farming.util.Constant.Companion.API_KEY_FOR_MARKET_PRICE
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PriceAPI {
    @GET("resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=$API_KEY_FOR_MARKET_PRICE&format=json&offset=0&limit=7000")
    fun getAPMC(@Query("limit") limit: Int): Call<APMCMain>

    @GET("resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=$API_KEY_FOR_MARKET_PRICE&format=json&offset=0&limit=7000")
    fun getAPMCBasedOnDistrict(@Query("district") district: String): Call<APMCMain>
}