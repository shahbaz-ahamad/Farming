package com.shahbaz.farming.mainscreen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.shahbaz.farming.R
import com.shahbaz.farming.databinding.FragmentProductDetailBinding
import com.shahbaz.farming.datamodel.CartItem
import com.shahbaz.farming.datamodel.Product
import com.shahbaz.farming.util.Resources
import com.shahbaz.farming.util.hideBottomNavigationBar
import com.shahbaz.farming.viewmodel.DetailFragmentViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProductDetailFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailBinding
    private val detailFragmentViewmodel by viewModels<DetailFragmentViewmodel>()

    //variable for the safeargs
    val args by navArgs<ProductDetailFragmentArgs>()
    var selectedQuantity: Int = 1
    var newPrice :Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //getting the data from the safeargs
        val product = args.product
        Log.d("product in detail", product.toString())
        newPrice = product.price?.toInt() ?: 0
        showData(product)

        if (args.source == "yourProduct") {
            binding.button.visibility = View.GONE
            binding.count.visibility = View.INVISIBLE
        }
        binding.callSeller.setOnClickListener {
            //onclick open the calling app with number
            val phoneNumber = product.phone
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneNumber")
            startActivity(intent)
        }


        binding.increaseQuantity.setOnClickListener {
            if (selectedQuantity < product.quantity!!.toInt()) {
                selectedQuantity = selectedQuantity + 1
                binding.selectedQuantity.text = selectedQuantity.toString()
                newPrice = selectedQuantity * product.price!!.toInt()
                binding.productPrice.text = "Rs:${newPrice.toString()}"
                Log.d("qunatity", selectedQuantity.toString())
            }
        }
        binding.decreaseQuantity.setOnClickListener {
            if (selectedQuantity > 1) {
                selectedQuantity = selectedQuantity - 1
                binding.selectedQuantity.text = selectedQuantity.toString()
                newPrice = selectedQuantity * product.price!!.toInt()
                binding.productPrice.text = "Rs:${newPrice.toString()}"
                Log.d("qunatity", selectedQuantity.toString())
            }
        }

        //add product to the card
        binding.saveProduct.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val cartItem = CartItem(
                productId = product.productId,
                productImage = product.productImage,
                title = product.title,
                quantity = selectedQuantity.toString(),
                price = newPrice.toString(),
                phone = product.phone,
                stock = product.stock,
                category = product.category,
                sellerId = product.sellerId,
                description = product.description,
                selectedQuantity = selectedQuantity
            )
            detailFragmentViewmodel.addProductToCart(cartItem)
        }

        observeAddingProductToCart()
    }

    private fun observeAddingProductToCart() {
        lifecycleScope.launch {
            detailFragmentViewmodel.addProductToCart.collect {
                when (it) {
                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), it.data.toString(), Toast.LENGTH_SHORT)
                    }

                    is Resources.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> {

                    }
                }
            }
        }
    }

    private fun showData(product: Product) {
        binding.apply {
            Glide.with(requireContext()).load(product.productImage).into(productImage)
            productTitle.text = product.title
            productPrice.text = "Rs:${product.price}"
            sellerContactNumber.text = "Contact Number:${product.phone}"
            productQty.text = "Qty:${product.quantity}"
            productDescription.text = product.description
        }
    }

    override fun onStart() {
        super.onStart()
        hideBottomNavigationBar()
    }
}