package com.shahbaz.farming.mainscreen

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.shahbaz.farming.R
import com.shahbaz.farming.databinding.FragmentAddProductBinding
import com.shahbaz.farming.datamodel.Product
import com.shahbaz.farming.util.Resources
import com.shahbaz.farming.util.progressDialgoue
import com.shahbaz.farming.viewmodel.AddProductViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AddProductFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentAddProductBinding
    private lateinit var progressDialog: ProgressDialog
    private val addProductViewmodel by viewModels<AddProductViewmodel>()
    private val args by navArgs<AddProductFragmentArgs>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    var selectedImageUrl: String = ""
    var selectedStock: String = ""
    var selectedCategory: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = ProgressDialog(requireContext())

        //setup spinner
        val stockSpinner = binding.stockSpinner
        val categorySpinner = binding.categorySpinner
        stockSpinner.onItemSelectedListener = this
        categorySpinner.onItemSelectedListener = this

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.stock_list,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            stockSpinner.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.category_list,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            categorySpinner.adapter = adapter
        }


        binding.productImage.setOnClickListener {
            imageLauncer.launch("image/*")
        }

        binding.addProduct.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val quantity = binding.etQuantity.text.toString()
            val price = binding.etPrice.text.toString()
            val phone = binding.etPhoneNumber.text.toString()
            val description = binding.etDescription.text.toString()

            if (title.isEmpty() || quantity.isEmpty() || price.isEmpty() || phone.isEmpty() || selectedCategory.isEmpty() || selectedStock.isEmpty() || selectedImageUrl.isEmpty() || description.isEmpty()) {
                //show toast
                Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT)
                    .show()
            } else {
                progressDialgoue(
                    progressDialog,
                    "Uploading Product",
                    "Product Upload In Progress"
                )
                //add product to the firestore
                addProductViewmodel.addProduct(
                    productImage = selectedImageUrl,
                    productTitle = title,
                    productQuantity = quantity,
                    productPrice = price,
                    sellerPhoneNumber = phone,
                    productStatus = selectedStock,
                    productCategory = selectedCategory,
                    productDescription = description

                )
            }
        }

        observeAddingOfProduct()


        handleArgumentPassed()

        binding.updateProduct.setOnClickListener {

            progressDialgoue(progressDialog, "Updating Product...", "Update in Progress")
            val quantity = binding.etQuantity.text.toString()
            val price = binding.etPrice.text.toString()
            val phone = binding.etPhoneNumber.text.toString()
            val description = binding.etDescription.text.toString()

            if (selectedImageUrl.isEmpty()) {
                val argument = args.productUpdate
                argument?.let {
                    val product = Product(
                        productId = it.productId,
                        productImage = it.productImage,
                        title = it.title,
                        quantity = quantity,
                        price = price,
                        phone = phone,
                        description = description,
                        sellerId = it.sellerId,
                        stock = selectedStock,
                        category = selectedCategory
                    )
                    addProductViewmodel.updateProduct(argument.productId!!, product)
                }
            } else {
                val argument = args.productUpdate
                argument?.let {
                    val product = Product(
                        productId = it.productId,
                        productImage = selectedImageUrl,
                        title = it.title,
                        quantity = quantity,
                        price = price,
                        description = description,
                        sellerId = it.sellerId,
                        stock = selectedStock,
                        category = selectedCategory
                    )
                    addProductViewmodel.updateProductwithImage(argument.productId!!, product)
                }
            }

        }

        observeUpdate()

    }

    private fun observeUpdate() {
        lifecycleScope.launch {
            addProductViewmodel.updateProductStatus.collect {
                when (it) {
                    is Resources.Error -> {
                        progressDialog.hide()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        addProductViewmodel.updateProductStatus
                    }

                    is Resources.Loading -> {

                    }

                    is Resources.Success -> {
                        progressDialog.hide()
                        Toast.makeText(requireContext(), it.data, Toast.LENGTH_SHORT).show()
                        clearAllEditText()
                        addProductViewmodel.resetUpdateState()
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun handleArgumentPassed() {
        val product = args.productUpdate
        product?.let {
            binding.apply {
                Glide.with(requireContext()).load(product.productImage).into(productImage)
                etTitle.setText(product.title)
                etTitle.isEnabled = false
                etQuantity.setText(product.quantity)
                etPrice.setText(product.price)
                etPhoneNumber.setText(product.phone)
                etDescription.setText(product.description)
                updateProduct.visibility = View.VISIBLE
                addProduct.visibility = View.GONE
                //for the stock spinnner
                val stockAdapter = stockSpinner.adapter as ArrayAdapter<String>
                val position = stockAdapter.getPosition(product.stock)
                stockSpinner.setSelection(position, false)
                selectedStock = product.stock.toString()
                //for the category spinner
                val categoryAdapter = categorySpinner.adapter as ArrayAdapter<String>
                val categoryPosition = categoryAdapter.getPosition(product.category)
                categorySpinner.setSelection(categoryPosition, false)
                selectedCategory = product.category.toString()
            }
        }
    }

    private fun observeAddingOfProduct() {
        lifecycleScope.launch {
            addProductViewmodel.productStatus.collect {
                when (it) {
                    is Resources.Error -> {
                        progressDialog.hide()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    is Resources.Loading -> {

                    }

                    is Resources.Success -> {
                        progressDialog.hide()
                        Toast.makeText(requireContext(), it.data, Toast.LENGTH_SHORT).show()
                        clearAllEditText()
                        addProductViewmodel.resetProductStatus()

                    }

                    is Resources.Unspecified -> {

                    }
                }
            }
        }
    }

    private fun clearAllEditText() {
        binding.apply {
            productImage.setImageResource(R.drawable.baseline_add_24)
            etTitle.text.clear()
            etQuantity.text.clear()
            etPrice.text.clear()
            etPhoneNumber.text.clear()
            etDescription.text.clear()
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        when (parent?.id) {
            R.id.stock_spinner -> {
                selectedStock = parent.getItemAtPosition(position) as String
                Log.d("stock", "onItemSelected: $selectedStock")
            }

            R.id.category_spinner -> {
                selectedCategory = parent.getItemAtPosition(position) as String
                Log.d("category", "onItemSelected: $selectedCategory")

            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //nothing selected

    }

    val imageLauncer = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedImageUrl = uri.toString()
            Glide.with(requireContext()).load(uri.toString()).into(binding.productImage)
        }
    }

}