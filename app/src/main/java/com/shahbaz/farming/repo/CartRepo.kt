package com.shahbaz.farming.repo

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.shahbaz.farming.datamodel.CartItem
import com.shahbaz.farming.util.Resources
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartRepo(
    val firestore: FirebaseFirestore
) {

    private val _cartitemStatus =
        MutableStateFlow<Resources<List<CartItem>>>(Resources.Unspecified())
    val cartItemStatus = _cartitemStatus.asStateFlow()

    private val _increaseQuaantiy =
        MutableStateFlow<Resources<String>>(Resources.Unspecified())
    val increaseQuantity = _increaseQuaantiy.asStateFlow()

    private val _decreaseQuaantiy =
        MutableStateFlow<Resources<String>>(Resources.Unspecified())
    val decreaseQuantity = _decreaseQuaantiy.asStateFlow()

    fun getCartItem() {
        _cartitemStatus.value = Resources.Loading()

        firestore.collection("FarmingCartItem")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    _cartitemStatus.value = Resources.Error(error.message.toString())
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val cartItems = snapshot.toObjects(CartItem::class.java)
                    _cartitemStatus.value = Resources.Success(cartItems)
                } else {
                    _cartitemStatus.value = Resources.Success(emptyList())
                }
            }
    }


    fun increaseQuantity(productId: String) {
        _increaseQuaantiy.value = Resources.Loading()
        //increse selected quantity when selectedQuantity is less than quantity
        firestore.collection("FarmingCartItem").document(productId).get()
            .addOnSuccessListener {
                val selectedQuantity = it.get("selectedQuantity")
                if (selectedQuantity.toString().toInt() < it.get("quantity").toString().toInt()) {
                    firestore.collection("FarmingCartItem").document(productId)
                        .update("selectedQuantity", FieldValue.increment(1))
                        .addOnSuccessListener {
                            _increaseQuaantiy.value = Resources.Success("Quantity Increased")
                        }
                        .addOnFailureListener {
                            _increaseQuaantiy.value = Resources.Error(it.message.toString())
                        }
                } else {
                    _increaseQuaantiy.value = Resources.Error("Out of Stock")
                }
            }
    }

    fun decreaseQuantity(productId: String) {
        _decreaseQuaantiy.value = Resources.Loading()
        firestore.collection("FarmingCartItem").document(productId).get()
            .addOnSuccessListener {
                val selectedQuantity = it.get("selectedQuantity")
                if (selectedQuantity.toString().toInt() > 1) {
                    firestore.collection("FarmingCartItem").document(productId)
                        .update("selectedQuantity", FieldValue.increment(-1))
                        .addOnSuccessListener {
                            _decreaseQuaantiy.value = Resources.Success("Quantity Decreased")
                        }
                        .addOnFailureListener {
                            _decreaseQuaantiy.value = Resources.Error(it.message.toString())
                        }
                } else {
                    _decreaseQuaantiy.value = Resources.Error("Quantity can't be less than 1")
                }
            }
    }


}