package com.shahbaz.farming.viewmodel

import androidx.lifecycle.ViewModel
import com.shahbaz.farming.repo.AddProductRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AddProductViewmodel @Inject constructor(
    private val addProductRepo: AddProductRepo
) : ViewModel() {

    val productStatus = addProductRepo.productStatus

    val yourProductStatus = addProductRepo.fetchYourProductStatus

    fun addProduct(
        productImage: String,
        productTitle: String,
        productQuantity: String,
        productPrice: String,
        sellerPhoneNumber: String,
        productStatus: String,
        productCategory: String,
        productDescription:String
    ) {
        addProductRepo.addProduct(
            productImage,
            productTitle,
            productQuantity,
            productPrice,
            sellerPhoneNumber,
            productStatus,
            productCategory,
            productDescription
        )
    }


    fun fetchYourProduct(){
        addProductRepo.fetchYourProduct()
    }
}