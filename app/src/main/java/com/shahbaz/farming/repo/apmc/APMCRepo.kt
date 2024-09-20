package com.shahbaz.farming.repo.apmc

import android.content.Context
import android.util.Log
import com.shahbaz.farming.datamodel.apmc.APMCMain
import com.shahbaz.farming.repo.LanguageChangeRepo
import com.shahbaz.farming.retrofit.PriceAPI
import com.shahbaz.farming.util.LanguageTranslator
import com.shahbaz.farming.util.Resources
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class APMCRepo(
    private val priceAPI: PriceAPI,
    private val languageChangeRepo: LanguageChangeRepo,
) {

    private val _priceStatus = MutableStateFlow<Resources<APMCMain>>(Resources.Unspecified())
    val priceStatus = _priceStatus.asStateFlow()

    private val _priceStatusbasedOnDistrict =
        MutableStateFlow<Resources<APMCMain>>(Resources.Unspecified())
    val priceStatusbasedOnDistrict = _priceStatus.asStateFlow()

    fun fetchApmcData() {
        _priceStatus.value = Resources.Loading()
        priceAPI.getAPMC(200).enqueue(object : Callback<APMCMain> {
            override fun onResponse(call: Call<APMCMain>, response: Response<APMCMain>) {
                if (response.isSuccessful) {
                    _priceStatus.value = Resources.Success(response.body()!!)
                }
            }

            override fun onFailure(call: Call<APMCMain>, t: Throwable) {
                _priceStatus.value = Resources.Error(t.localizedMessage)
            }
        })
    }

    fun fetchApmcDataBasedOnDistrict() {
        _priceStatusbasedOnDistrict.value = Resources.Loading()
        priceAPI.getAPMCBasedOnDistrict("Rajkot").enqueue(object : Callback<APMCMain> {
            override fun onResponse(call: Call<APMCMain>, response: Response<APMCMain>) {
                if (response.isSuccessful) {
                    _priceStatusbasedOnDistrict.value = Resources.Success(response.body()!!)
                }
            }

            override fun onFailure(call: Call<APMCMain>, t: Throwable) {
                _priceStatusbasedOnDistrict.value = Resources.Error(t.localizedMessage)
            }
        })
    }


}