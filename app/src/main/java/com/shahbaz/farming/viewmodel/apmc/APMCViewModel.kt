package com.shahbaz.farming.viewmodel.apmc

import androidx.lifecycle.ViewModel
import com.shahbaz.farming.repo.apmc.APMCRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class APMCViewModel @Inject constructor(
    private val apmcRepository: APMCRepo,
) : ViewModel() {

    val apmPriceState = apmcRepository.priceStatus
    val priceStatusbasedOnDistrict = apmcRepository.priceStatusbasedOnDistrict

    fun fetchAPMCData() {
        apmcRepository.fetchApmcData()
    }

    fun fetchBasedOnDistrict() {
        apmcRepository.fetchApmcDataBasedOnDistrict()
    }
}