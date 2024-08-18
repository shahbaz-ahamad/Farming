package com.shahbaz.farming.repo

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.shahbaz.farming.datamodel.CartItem
import com.shahbaz.farming.util.Resources
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartRepo(
    val firestore: FirebaseFirestore,
    val firebaseAuth: FirebaseAuth
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

    private val _deleteStatus = MutableStateFlow<Resources<String>>(Resources.Unspecified())
    val deleteStatus = _deleteStatus.asStateFlow()

    fun getCartItem() {
        _cartitemStatus.value = Resources.Loading()

        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            firestore.collection("FarmingCartItem")
                .document(currentUser.uid)
                .collection(currentUser.displayName!!)
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
        } else {
            _cartitemStatus.value = Resources.Error("Please Login")
        }
    }


    fun increaseQuantity(productId: String) {
        _increaseQuaantiy.value = Resources.Loading()
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            // Increase selected quantity when selectedQuantity is less than quantity
            val docRef = firestore.collection("FarmingCartItem")
                .document(currentUser.uid)
                .collection(currentUser.displayName!!)
                .document(productId)

            docRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    val selectedQuantity = document.get("selectedQuantity")
                    val quantity = document.get("quantity")
                    if (selectedQuantity.toString().toInt() < quantity.toString().toInt()) {
                        docRef.update("selectedQuantity", FieldValue.increment(1))
                            .addOnSuccessListener {
                                _increaseQuaantiy.value = Resources.Success("Quantity Increased")
                            }
                            .addOnFailureListener {
                                _increaseQuaantiy.value = Resources.Error(it.message.toString())
                                Log.d("message", it.localizedMessage)
                            }
                    } else {
                        _increaseQuaantiy.value = Resources.Error("Out of Stock")
                    }
                } else {
                    _increaseQuaantiy.value = Resources.Error("Product not found")
                }
            }.addOnFailureListener {
                _increaseQuaantiy.value = Resources.Error(it.message.toString())
            }
        } else {
            _increaseQuaantiy.value = Resources.Error("Please Login")
        }
    }


    fun decreaseQuantity(productId: String) {
        _decreaseQuaantiy.value = Resources.Loading()
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            // Decrease selected quantity when selectedQuantity is greater than 1
            val docRef = firestore.collection("FarmingCartItem")
                .document(currentUser.uid)
                .collection(currentUser.displayName!!)
                .document(productId)

            docRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    val selectedQuantity = document.get("selectedQuantity")
                    if (selectedQuantity.toString().toInt() > 1) {
                        docRef.update("selectedQuantity", FieldValue.increment(-1))
                            .addOnSuccessListener {
                                _decreaseQuaantiy.value = Resources.Success("Quantity Decreased")
                            }
                            .addOnFailureListener {
                                _decreaseQuaantiy.value = Resources.Error(it.message.toString())
                            }
                    } else {
                        _decreaseQuaantiy.value = Resources.Error("Quantity can't be less than 1")
                    }
                } else {
                    _decreaseQuaantiy.value = Resources.Error("Product not found")
                }
            }.addOnFailureListener {
                _decreaseQuaantiy.value = Resources.Error(it.message.toString())
            }
        } else {
            _decreaseQuaantiy.value = Resources.Error("Please Login")
        }
    }


    fun deleteCartItem(productId: String) {
        _deleteStatus.value = Resources.Loading()
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            firestore.collection("FarmingCartItem")
                .document(currentUser.uid)
                .collection(currentUser.displayName!!)
                .document(productId)
                .delete()
                .addOnSuccessListener {
                    _deleteStatus.value = Resources.Success("Deleted")
                }
                .addOnFailureListener {
                    _deleteStatus.value = Resources.Error(it.localizedMessage)
                }

        } else {
            _deleteStatus.value = Resources.Error("Please Login")
        }
    }

    fun resetDeleteStatus() {
        _deleteStatus.value = Resources.Unspecified()
    }
}