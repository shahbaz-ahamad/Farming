package com.shahbaz.farming.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahbaz.farming.datamodel.Order
import com.shahbaz.farming.repo.OrderRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewmodel @Inject constructor(
    private val orderRepo: OrderRepo,
) : ViewModel() {
    val fetchOrder = orderRepo.fetchOrder

    val fetchYourSelledOrder = orderRepo.fetchYourSelledOrder

    val updateOrderStatus = orderRepo.updateOrderStatus

    val orderPlaced = orderRepo.countOrderPlaced
    val orderReceived = orderRepo.countOrderReceived


    fun fetchOrder() {
        orderRepo.fetchOrder()
    }

    fun fetchOrderReceived() {
        orderRepo.fetchOrderReceived()
    }

    fun updateOrderStatus(order: Order, status: String) {
        orderRepo.updateOrderStatus(order, status)
    }

    fun getOrderPlacedCount() {
        orderRepo.countOrderPlaced()
    }

    fun getOrderReceived() {
        orderRepo.countOrderReceived()
    }


}

