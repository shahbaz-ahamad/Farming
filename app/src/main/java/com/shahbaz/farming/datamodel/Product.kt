package com.shahbaz.farming.datamodel

data class Product(
    val productId: String? = "",
    val productImage: String? = "",
    val title: String? = "",
    val quantity: String? = "",
    val price: String? = "",
    val phone: String? = "",
    val stock: String? = "",
    val category: String? = "",
    val sellerId:String?="",
    val description:String?=""
)