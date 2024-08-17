package com.shahbaz.farming.repo

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.shahbaz.farming.datamodel.Product
import com.shahbaz.farming.util.Resources
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddProductRepo(
    val firebaseAuth: FirebaseAuth,
    val firebaseFirestore: FirebaseFirestore,
    val firbaseStorage: FirebaseStorage
) {

    private val _productStatus = MutableStateFlow<Resources<String>>(Resources.Unspecified())
    val productStatus = _productStatus.asStateFlow()

    private val _fetchYourProductStatus =
        MutableStateFlow<Resources<List<Product>>>(Resources.Unspecified())
    val fetchYourProductStatus = _fetchYourProductStatus.asStateFlow()


    private val _fetchOthersListedProductStatus =
        MutableStateFlow<Resources<List<Product>>>(Resources.Unspecified())
    val fetchOthersListedProductStatus = _fetchOthersListedProductStatus.asStateFlow()


    fun addProduct(
        productImage: String,
        productTitle: String,
        productQuantity: String,
        productPrice: String,
        sellerPhoneNumber: String,
        productStatus: String,
        productCategory: String,
        productDescription: String
    ) {
        _productStatus.value = Resources.Loading()
        val currentUserUid = firebaseAuth.currentUser?.uid.toString()

        firbaseStorage.getReference("FarmingProduct")
            .child(System.currentTimeMillis().toString() + productTitle)
            .putFile(Uri.parse(productImage))
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { url ->

                    val product = Product(
                        productId = System.currentTimeMillis().toString() + productTitle,
                        productImage = url.toString(),
                        title = productTitle,
                        quantity = productQuantity,
                        price = productPrice,
                        phone = sellerPhoneNumber,
                        stock = productStatus,
                        category = productCategory,
                        sellerId = currentUserUid,
                        description = productDescription
                    )
                    firebaseFirestore.collection("FarmingProduct")
                        .document()
                        .set(product)
                        .addOnSuccessListener {
                            _productStatus.value = Resources.Success("Product Added")
                        }
                        .addOnFailureListener {
                            _productStatus.value = Resources.Error(it.localizedMessage)
                        }

                }
                    .addOnFailureListener {
                        _productStatus.value = Resources.Error(it.localizedMessage)
                    }
            }


    }


    fun fetchYourProduct() {
        _fetchYourProductStatus.value = Resources.Loading()
        val currentUserUid = firebaseAuth.currentUser?.uid.toString()


        firebaseFirestore.collection("FarmingProduct").whereEqualTo("sellerId", currentUserUid)
            .get()
            .addOnSuccessListener {
                val productList = it.toObjects(Product::class.java)
                _fetchYourProductStatus.value = Resources.Success(productList)
            }
            .addOnFailureListener {
                _fetchYourProductStatus.value = Resources.Error(it.localizedMessage)
            }
    }

    fun fetchOthersListedProduct() {
        _fetchOthersListedProductStatus.value = Resources.Loading()

        //fetch the product where sellerId doesn't equal to the current user id
        firebaseFirestore.collection("FarmingProduct")
            .whereNotEqualTo("sellerId", firebaseAuth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener {
                val productList = it.toObjects(Product::class.java)
                _fetchOthersListedProductStatus.value = Resources.Success(productList)
            }
            .addOnFailureListener {
                _fetchOthersListedProductStatus.value = Resources.Error(it.localizedMessage)
            }
    }

    fun resetProductStatus(){
        _productStatus.value = Resources.Unspecified()
    }

}