package com.shahbaz.farming.notification


import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.shahbaz.farming.repo.BillingRepo
import com.shahbaz.farming.viewmodel.BillingViewmodel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessaginService : FirebaseMessagingService() {

    @Inject
    lateinit var billingRepo: BillingRepo

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        try {
            Log.d("MyFirebaseService", "onNewToken called")
            Log.d("MyFirebaseService", "Token: $token")
            billingRepo.updateFCMToken(token)
            Log.d("MyFirebaseService", "updateFCMToken called")
        } catch (e: Exception) {
            Log.e("MyFirebaseService", "Error in onNewToken", e)
        }
    }


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        //handle on the message received
    }
}
