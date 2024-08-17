package com.shahbaz.farming.datamodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItem(
    val productId: String? = "",
    val productImage: String? = "",
    val title: String? = "",
    val quantity: String? = "",
    val price: String? = "",
    val phone: String? = "",
    val stock: String? = "",
    val category: String? = "",
    val sellerId: String? = "",
    val description: String? = "",
    val selectedQuantity: Int? = 1
) : Parcelable
