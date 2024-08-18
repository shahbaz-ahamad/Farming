package com.shahbaz.farming.viewmodel

import androidx.lifecycle.ViewModel
import com.shahbaz.farming.repo.CartRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartItemViewmodel @Inject constructor(
    private val cartRepo: CartRepo
) :ViewModel(){

    val cartItemStatus = cartRepo.cartItemStatus
    val increaseQuantity = cartRepo.increaseQuantity
    val decreaseQuantity = cartRepo.decreaseQuantity
    val deleteStatus = cartRepo.deleteStatus

    fun getCartItem(){
        cartRepo.getCartItem()
    }

    fun increaseQuantity(prodcutId:String){
        cartRepo.increaseQuantity(prodcutId)
    }

    fun decreaseQuantity(prodcutId:String){
        cartRepo.decreaseQuantity(prodcutId)
    }

    fun deleteCartItem(prodcutId: String){
        cartRepo.deleteCartItem(prodcutId)
    }

    fun resetDeleteStatus(){
        cartRepo.resetDeleteStatus()
    }
}