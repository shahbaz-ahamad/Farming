package com.shahbaz.farming.repo

import android.net.Uri
import android.util.Log
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


<<<<<<< HEAD
    private val _fetchOthersListedProductStatus =
        MutableStateFlow<Resources<List<Product>>>(Resources.Unspecified())
    val fetchOthersListedProductStatus = _fetchOthersListedProductStatus.asStateFlow()

    private val _deleteProduct = MutableStateFlow<Resources<String>>(Resources.Unspecified())
    val deleteProduct = _deleteProduct.asStateFlow()


    private val _updateProduct = MutableStateFlow<Resources<String>>(Resources.Unspecified())
    val updateProuct = _updateProduct.asStateFlow()


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
        productDescription: String
    ) {
        _productStatus.value = Resources.Loading()
        val currentUserUid = firebaseAuth.currentUser?.uid.toString()
        val productId = System.currentTimeMillis().toString() + productTitle

        firbaseStorage.getReference("FarmingProduct")
            .child(System.currentTimeMillis().toString() + productTitle)
            .putFile(Uri.parse(productImage))
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener {url ->

                    val product = Product(
                        productId = productId,
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
                        .document(productId)
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
<<<<<<< HEAD

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

    fun resetProductStatus() {
        _productStatus.value = Resources.Unspecified()
    }


    fun deleteProduct(productID: String) {
        _deleteProduct.value = Resources.Loading()
        //code to delete the product
        Log.d("ProductId", productID)
        //write the code to delete the product where productId is equal to passed productId
        firebaseFirestore.collection("FarmingProduct").document(productID)
            .delete()
            .addOnSuccessListener {
                _deleteProduct.value = Resources.Success("Product Deleted")
            }
            .addOnFailureListener {
                _deleteProduct.value = Resources.Error(it.localizedMessage)
            }
    }

    fun updateProductWithImage(productID: String, product: Product) {
        _updateProduct.value = Resources.Loading()

        firbaseStorage.getReference("FarmingProduct")
            .child(System.currentTimeMillis().toString() + product.title)
            .putFile(Uri.parse(product.productImage))
            .addOnSuccessListener { snapshot ->
                snapshot.storage.downloadUrl.addOnSuccessListener { url ->
                    val product = Product(
                        productId = product.productId,
                        productImage = url.toString(),
                        title = product.title,
                        quantity = product.quantity,
                        price = product.price,
                        phone = product.phone,
                        stock = product.stock,
                        category = product.category,
                        sellerId = product.sellerId,
                        description = product.description
                    )

                    firebaseFirestore.collection("FarmingProduct").document(productID)
                        .set(product)
                        .addOnSuccessListener {
                            _updateProduct.value = Resources.Success("Product Updated")
                        }
                        .addOnFailureListener {
                            _updateProduct.value = Resources.Error(it.localizedMessage)
                        }


                }
                    .addOnFailureListener {
                        _updateProduct.value = Resources.Error(it.localizedMessage)
                    }
            }
            .addOnFailureListener {
                _updateProduct.value = Resources.Error(it.localizedMessage)

            }
        //write the code to update the product
        firebaseFirestore.collection("FarmingProduct").document(productID)
        //.update()
    }

    fun updateProductWithOutImageChange(productID: String, product: Product) {
        _updateProduct.value = Resources.Loading()
        firebaseFirestore.collection("FarmingProduct").document(productID)
            .set(product)
            .addOnSuccessListener {
                _updateProduct.value = Resources.Success("Product Updated")
            }
            .addOnFailureListener {
                _updateProduct.value = Resources.Error(it.localizedMessage)
            }
    }

    fun resetUpdateStatus(){
        _updateProduct.value = Resources.Unspecified()
    }
=======
>>>>>>> 36a6260 (detached brahcned)
}