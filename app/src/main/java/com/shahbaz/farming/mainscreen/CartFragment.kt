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
import com.shahbaz.farming.adapter.CartItemAdapter
import com.shahbaz.farming.databinding.FragmentCartBinding
import com.shahbaz.farming.datamodel.CartItem
import com.shahbaz.farming.util.Resources
import com.shahbaz.farming.util.hideBottomNavigationBar
import com.shahbaz.farming.viewmodel.CartItemViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private val cartItemAdapter by lazy {
        CartItemAdapter()
    }
    private val cartViewmodel by viewModels<CartItemViewmodel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartViewmodel.getCartItem()
        setUpRecyclerview()
        observeCartItem()

        cartItemAdapter.onPlusClick = {
            cartViewmodel.increaseQuantity(it.productId!!)
        }

        cartItemAdapter.onMinusClick = {
            cartViewmodel.decreaseQuantity(it.productId!!)
        }

        cartItemAdapter.onDeleteClick = {
            cartViewmodel.deleteCartItem(it)
        }

        cartItemAdapter.onBuyNowClick = {
            val action = CartFragmentDirections.actionCartFragmentToBillingFragment(it)
            findNavController().navigate(action)
        }
        observeIncreaseQuantity()
        observeDecreaseQuantity()
        observeDelete()

    }

    private fun observeDelete() {
        lifecycleScope.launch {
            cartViewmodel.deleteStatus.collect {
                when (it) {
                    is Resources.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.GONE
                    }

                    is Resources.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Resources.Success -> {
                        Toast.makeText(requireContext(), it.data.toString(), Toast.LENGTH_SHORT)
                            .show()
                        binding.progressBar.visibility = View.GONE
                        cartViewmodel.resetDeleteStatus()
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun observeDecreaseQuantity() {
        lifecycleScope.launch {
            cartViewmodel.decreaseQuantity.collect {
                when (it) {
                    is Resources.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.GONE
                    }

                    is Resources.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Resources.Success -> {
                        binding.progressBar.visibility = View.GONE
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun observeIncreaseQuantity() {
        lifecycleScope.launch {
            cartViewmodel.increaseQuantity.collect {
                when (it) {
                    is Resources.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.GONE
                    }

                    is Resources.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Resources.Success -> {
                        binding.progressBar.visibility = View.GONE
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun observeCartItem() {
        lifecycleScope.launch {
            cartViewmodel.cartItemStatus.collect {
                when (it) {
                    is Resources.Error -> {
                        Log.d("cartdata", it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.GONE
                    }

                    is Resources.Loading -> {
                        Log.d("CartData", "Loading")
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Resources.Success -> {
                        if (it.data!!.isEmpty()) {
                            binding.cartSummary.visibility = View.GONE
                            binding.progressBar.visibility = View.GONE
                        } else {
                            cartItemAdapter.differ.submitList(it.data)
                            Log.d("CartData", it.data.toString())
                            binding.progressBar.visibility = View.GONE
                            showCartSummary(it.data)
                        }
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun showCartSummary(data: List<CartItem>) {
        val size = data.size
        val totalCost = data.sumOf { it.price!!.toInt() }
        binding.totalItem.text = "Total Item:$size"
        binding.totalCost.text = "Total Cost:Rs$totalCost"
    }

    fun setUpRecyclerview() {
        binding.cartItemRecyclerview.apply {
            adapter = cartItemAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }

    override fun onStart() {
        super.onStart()
        hideBottomNavigationBar()
    }
}