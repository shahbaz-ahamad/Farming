package com.shahbaz.farming.datamodel

data class Order(
    val orderId: String = "",
    val sellerId:String ="",
    val buyerId: String = "",
    val orderStatus: String = "",
    val product: CartItem = CartItem(),
    val address: Address = Address()
)
