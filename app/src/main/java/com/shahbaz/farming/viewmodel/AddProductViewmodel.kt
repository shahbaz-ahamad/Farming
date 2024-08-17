package com.shahbaz.farming.viewmodel

import androidx.lifecycle.ViewModel
import com.shahbaz.farming.datamodel.Product
import com.shahbaz.farming.repo.AddProductRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AddProductViewmodel @Inject constructor(
    private val addProductRepo: AddProductRepo
) : ViewModel() {

    val productStatus = addProductRepo.productStatus

    val yourProductStatus = addProductRepo.fetchYourProductStatus

<<<<<<< HEAD
    val fetchOtherProductStatus = addProductRepo.fetchOthersListedProductStatus

    val deleteProductStatus = addProductRepo.deleteProduct

    val updateProductStatus = addProductRepo.updateProuct

=======
>>>>>>> 36a6260 (detached brahcned)
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
<<<<<<< HEAD


    fun fetchOtherProduct() {
        addProductRepo.fetchOthersListedProduct()
    }

    fun resetProductStatus() {
        addProductRepo.resetProductStatus()
    }

    fun deleteProduct(productId: String) {
        addProductRepo.deleteProduct(productId)
    }

    fun updateProductwithImage(productID: String, product: Product) {
        addProductRepo.updateProductWithImage(productID, product)
    }

    fun updateProduct(productID: String, product: Product) {
        addProductRepo.updateProductWithOutImageChange(productID, product)

    }

    fun resetUpdateState(){
        addProductRepo.resetUpdateStatus()
    }
=======
>>>>>>> 36a6260 (detached brahcned)
}