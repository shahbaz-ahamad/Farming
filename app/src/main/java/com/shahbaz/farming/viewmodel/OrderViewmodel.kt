package com.shahbaz.farming.viewmodel

import androidx.lifecycle.ViewModel
import com.shahbaz.farming.datamodel.Order
import com.shahbaz.farming.repo.OrderRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderViewmodel @Inject constructor(
    private val orderRepo: OrderRepo
) : ViewModel() {
    val fetchOrder = orderRepo.fetchOrder

    val fetchYourSelledOrder = orderRepo.fetchYourSelledOrder

    val updateOrderStatus = orderRepo.updateOrderStatus

    fun fetchOrder() {
        orderRepo.fetchOrder()
    }

    fun fetchOrderReceived() {
        orderRepo.fetchOrderReceived()
    }

    fun updateOrderStatus(order: Order, status: String) {
        orderRepo.updateOrderStatus(order, status)
    }

}

