package com.shahbaz.farming.repo

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.shahbaz.farming.datamodel.Address
import com.shahbaz.farming.datamodel.Order
import com.shahbaz.farming.util.Resources
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class BillingRepo(
    val firestore: FirebaseFirestore,
    val firebaseAuth: FirebaseAuth,
    val firebaseMessaging: FirebaseMessaging,
) {

    private val _addressStatus = MutableStateFlow<Resources<String>>(Resources.Unspecified())
    val addressStatus = _addressStatus.asStateFlow()

    private val _fetchAddress = MutableStateFlow<Resources<List<Address>>>(Resources.Unspecified())
    val fetchAddress = _fetchAddress.asStateFlow()

    private val _placeOrder = MutableStateFlow<Resources<String>>(Resources.Unspecified())
    val placeOrder = _placeOrder.asStateFlow()


    fun addAddress(address: Address) {
        _addressStatus.value = Resources.Loading()
        firestore.collection("FarmerUser")
            .document(firebaseAuth.currentUser!!.uid)
            .collection("Address")
            .document()
            .set(address)
            .addOnSuccessListener {
                _addressStatus.value = Resources.Success("Success")
            }
            .addOnFailureListener {
                _addressStatus.value = Resources.Error(it.localizedMessage)
            }
    }

    fun resetAddressStatus() {
        _addressStatus.value = Resources.Unspecified()
    }

    fun fetchAddress() {
        _fetchAddress.value = Resources.Loading()
        firestore.collection("FarmerUser").document(firebaseAuth.currentUser!!.uid)
            .collection("Address")
            .get()
            .addOnSuccessListener {
                _fetchAddress.value = Resources.Success(it.toObjects(Address::class.java))
            }
            .addOnFailureListener {
                _fetchAddress.value = Resources.Error(it.localizedMessage)
            }
    }


    fun placeOrder(order: Order) {
        _placeOrder.value = Resources.Loading()
        firestore.runBatch {
            firestore.collection("FarmingProductOrder")
                .document(firebaseAuth.currentUser!!.uid)
                .set(order)

            firestore.collection("FarmingCartItem")
                .document(firebaseAuth.currentUser!!.uid)
                .collection(firebaseAuth.currentUser!!.displayName!!)
                .document(order.product.productId!!)
                .delete()
        }
            .addOnSuccessListener {
                _placeOrder.value = Resources.Success("Success")
            }
            .addOnFailureListener {
                _placeOrder.value = Resources.Error(it.localizedMessage)
            }
    }


    fun getCurrentUserId(): String {
        return firebaseAuth.currentUser!!.uid
    }


    fun updateFCMToken(token: String) {
        firestore.collection("FarmerUser").document(firebaseAuth.currentUser!!.uid)
            .update("fcmToken", token)
        Log.d("Token", token)

    }
}