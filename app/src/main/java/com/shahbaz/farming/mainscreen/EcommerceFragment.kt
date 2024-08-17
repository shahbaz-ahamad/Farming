package com.shahbaz.farming.mainscreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shahbaz.farming.R
import com.shahbaz.farming.adapter.CatgoryApdater
import com.shahbaz.farming.adapter.OtherProductAdapter
import com.shahbaz.farming.databinding.FragmentEcommerceBinding
import com.shahbaz.farming.datamodel.Product
import com.shahbaz.farming.util.Constant.Companion.CATEGORY_LIST
import com.shahbaz.farming.util.Resources
import com.shahbaz.farming.viewmodel.AddProductViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class EcommerceFragment : Fragment() {


    private lateinit var binding: FragmentEcommerceBinding
    private val categoryAdapter by lazy { CatgoryApdater(CATEGORY_LIST) }
    private val otherProductAdapter by lazy { OtherProductAdapter() }
    private val addProductViewmodel by viewModels<AddProductViewmodel>()
    private lateinit var productList: List<Product>
    private lateinit var filteredList: List<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEcommerceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productList = listOf()
        filteredList = listOf()

        setUpCategoryRecyclerview()
        categoryAdapter.selectedPosition = 0
        categoryAdapter.onCategoryClick = {
            Log.d("category", it.toString())
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()

            if (it == "All") {
                otherProductAdapter.differ.submitList(productList)
            } else {
                filteredList = productList.filter { product ->
                    product.category == it
                }
                otherProductAdapter.differ.submitList(filteredList)
            }
        }
        setupOtherProductRecyclerview()
        addProductViewmodel.fetchOtherProduct()
        observeOtherProduct()

        otherProductAdapter.onClick ={product ->
            val action = EcommerceFragmentDirections.actionEcommerceFragmentToProductDetailFragment(product,"ecommerce")
            findNavController().navigate(action)
        }

    }

    private fun observeOtherProduct() {
        lifecycleScope.launch {
            addProductViewmodel.fetchOtherProductStatus.collect {
                when (it) {
                    is Resources.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.GONE
                    }

                    is Resources.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Resources.Success -> {
                        Log.d("product", it.data.toString())
                        it.data?.let { product ->
                            productList = product
                            Log.d(
                                "observeOtherProduct",
                                "Submitting list to adapter: ${product.size} items"
                            )
                            otherProductAdapter.differ.submitList(product)
                        }
                        binding.progressBar.visibility = View.GONE
                    }

                    else -> Unit

                }
            }
        }
    }

    private fun setupOtherProductRecyclerview() {
        binding.otherProductRecyclerview.apply {
            adapter = otherProductAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }

    private fun setUpCategoryRecyclerview() {
        binding.categoryrecyclerview.apply {
            adapter = categoryAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }


}