package com.shahbaz.farming.viewmodel

import androidx.lifecycle.ViewModel
import com.shahbaz.farming.datamodel.Address
import com.shahbaz.farming.datamodel.Order
import com.shahbaz.farming.repo.BillingRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class BillingViewmodel @Inject constructor(
    private val billingRepo: BillingRepo
) : ViewModel() {

    val addressStatus = billingRepo.addressStatus
    val fetchAddress = billingRepo.fetchAddress
    val placeOrder = billingRepo.placeOrder

    fun addAddress(address: Address) {
        billingRepo.addAddress(address)
    }

    fun resetAddressStatus(){
        billingRepo.resetAddressStatus()
    }

    fun fetchAddress(){
        billingRepo.fetchAddress()
    }
    fun placeOrder(order: Order){
        billingRepo.placeOrder(order)
    }

    fun getCurrentUserId():String{
        return billingRepo.getCurrentUserId()
    }

    fun updateFCMtoken(token:String){
        billingRepo.updateFCMToken(token)
    }

}