package com.shahbaz.farming.notification

import com.shahbaz.farming.util.OuthToken
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationApi {
    @Headers(
        "Content-Type:application/json"
    )
    @POST("messages:send")
    fun sendNotification(
        @Header("Authorization") authToken: String,
        @Body message: NotifcationDataClass,
    ): Call<Unit>
}