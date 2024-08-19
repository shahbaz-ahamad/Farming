package com.shahbaz.farming.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.shahbaz.farming.datamodel.Order
import com.shahbaz.farming.util.Resources
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class OrderRepo(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {

    private val _fetchorder = MutableStateFlow<Resources<List<Order>>>(Resources.Unspecified())
    val fetchOrder = _fetchorder.asStateFlow()

    private val _fetchYourSelledOrder =
        MutableStateFlow<Resources<List<Order>>>(Resources.Unspecified())
    val fetchYourSelledOrder = _fetchYourSelledOrder.asStateFlow()

    private val _updateOrderStatus = MutableStateFlow<Resources<String>>(Resources.Unspecified())
    val updateOrderStatus = _updateOrderStatus.asStateFlow()

    fun fetchOrder() {
        _fetchorder.value = Resources.Loading()

        //fetch order where orderId is equal to currentUser id
        firestore.collection("FarmingProductOrder")
            .whereEqualTo("buyerId", firebaseAuth.currentUser!!.uid)
            .get()
            .addOnSuccessListener {
                _fetchorder.value = Resources.Success(it.toObjects(Order::class.java))
            }
            .addOnFailureListener {
                _fetchorder.value = Resources.Error(it.localizedMessage)
            }
    }

    fun fetchOrderReceived() {
        _fetchYourSelledOrder.value = Resources.Loading()
        firestore.collection("FarmingProductOrder")
            .whereEqualTo("sellerId", firebaseAuth.currentUser!!.uid)
            .get()
            .addOnSuccessListener {
                _fetchYourSelledOrder.value = Resources.Success(it.toObjects(Order::class.java))
            }
            .addOnFailureListener {
                _fetchYourSelledOrder.value = Resources.Error(it.localizedMessage)
            }
    }

    fun updateOrderStatus(order: Order, status: String) {
        _updateOrderStatus.value = Resources.Loading()
        firestore.collection("FarmingProductOrder")
            .document(order.buyerId)
            .update("orderStatus", status)
            .addOnSuccessListener {
                _updateOrderStatus.value = Resources.Success("Success")
            }
            .addOnFailureListener {
                _updateOrderStatus.value = Resources.Error(it.localizedMessage)
            }

    }
}