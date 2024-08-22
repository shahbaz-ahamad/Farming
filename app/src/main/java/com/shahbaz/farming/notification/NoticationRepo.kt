package com.shahbaz.farming.notification

import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.shahbaz.farming.datamodel.Order
import com.shahbaz.farming.util.OuthToken
import com.shahbaz.farming.util.Resources
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticationRepo(
    private val notificationApi: NotificationApi,
    private val firestore: FirebaseFirestore,
) {
    private val _notificationState = MutableStateFlow<Resources<String>>(Resources.Unspecified())
    val notificationState = _notificationState.asStateFlow()

    fun sendNotificationToSeller(order: Order) {

        val notification = Notification(
            title = "Order received",
            body = order.product.title.toString()
        )
        firestore.collection("FarmerUser").document(order.sellerId).get()
            .addOnSuccessListener {
                val token = it.getString("fcmToken")
                Log.d("token from firebase", token.toString())
                val message = Message(
                    token = token.toString(),
                    notification = notification
                )

                val notifcationDataClass = NotifcationDataClass(
                    message = message
                )

                CoroutineScope(Dispatchers.IO).launch {
                    val accessToken = OuthToken.getAccessToken()

                    Log.d("access token in repo", token.toString())
                    notificationApi.sendNotification(
                        authToken = "Bearer $accessToken",
                        message = notifcationDataClass
                    ).enqueue(object : Callback<Unit> {
                        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {

                            _notificationState.value = Resources.Success("Notification sent")
                        }

                        override fun onFailure(call: Call<Unit>, t: Throwable) {
                            _notificationState.value =
                                Resources.Error(t.localizedMessage.toString())
                        }

                    })


                }

            }
    }

}

