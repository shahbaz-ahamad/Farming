package com.shahbaz.farming.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.shahbaz.farming.datamodel.CartItem
import com.shahbaz.farming.util.Resources
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class Detailsfragmentrepo(
    private val firsestore: FirebaseFirestore
) {
    private val _addProductToCard = MutableStateFlow<Resources<String>>(Resources.Unspecified())
    val addProductToCard = _addProductToCard.asStateFlow()

    fun addProductToCart(cartItem: CartItem) {
        _addProductToCard.value = Resources.Loading()
        firsestore.collection("FarmingCartItem")
            .document(cartItem.productId!!)
            .set(cartItem)
            .addOnSuccessListener {
                _addProductToCard.value = Resources.Success("Product Added")
            }
            .addOnFailureListener {
                _addProductToCard.value = Resources.Error(it.message.toString())
            }
    }
}