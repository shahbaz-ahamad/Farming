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
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.shahbaz.farming.R
import com.shahbaz.farming.adapter.AddressAdapter
import com.shahbaz.farming.adapter.BillingProductAdapter
import com.shahbaz.farming.adapter.CartItemAdapter
import com.shahbaz.farming.databinding.FragmentBillingBinding
import com.shahbaz.farming.datamodel.Address
import com.shahbaz.farming.datamodel.Order
import com.shahbaz.farming.datamodel.OrderStatus
import com.shahbaz.farming.util.Resources
import com.shahbaz.farming.util.showDialogue
import com.shahbaz.farming.viewmodel.BillingViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class BillingFragment : Fragment() {

    private lateinit var binding: FragmentBillingBinding
    private val billingViewmodel by viewModels<BillingViewmodel>()
    private val args by navArgs<BillingFragmentArgs>()
    private val billingProductAdapter by lazy {
        BillingProductAdapter()
    }

    private val addressAdapter by lazy {
        AddressAdapter()
    }
    private var selectedAddress: Address? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBillingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpProductRecyclerview()
        setUpAddressRecyclerview()
        val cartItem = args.cartItem
        val list = listOf(
            cartItem
        )
        binding.totalPrice.text = "Total Price :Rs${cartItem.price}"
        billingProductAdapter.differ.submitList(list)

        binding.addAddress.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
        }

        billingViewmodel.fetchAddress()
        observeAddress()

        addressAdapter.onAddressItemClick = {
            selectedAddress = it
            Log.d("selected Address", selectedAddress.toString())
        }

        binding.buttonPlaceOrder.setOnClickListener {
            //check which radio button is checked
            if (binding.checkBoxCod.isChecked) {
                if (selectedAddress != null) {
                    //perform order placing
                    showDialogue(
                        context = requireContext(),
                        title = "Order Cart Item",
                        message = "Are You Sure..?",
                        positiveButton = "Ok",
                        negativeButton = "Cancel",
                        onClick = {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.buttonPlaceOrder.visibility = View.GONE

                            billingViewmodel.placeOrder(
                                Order(
                                    orderId = billingViewmodel.getCurrentUserId() + System.currentTimeMillis(),
                                    sellerId = cartItem.sellerId!!,
                                    buyerId = billingViewmodel.getCurrentUserId(),
                                    orderStatus = OrderStatus.Ordered.status,
                                    product = cartItem,
                                    address = selectedAddress!!
                                )
                            )
                        }
                    )
                } else {
                    Toast.makeText(requireContext(), "Please select an address", Toast.LENGTH_SHORT)
                        .show()
                }
            } else if (binding.checkBoxUpi.isChecked) {
                Toast.makeText(
                    requireContext(),
                    "Currently UPI Payement in Unavilable",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            } else {
                Toast.makeText(requireContext(), "Please Select Payment Method", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
        }

        observePlaceOrder()
    }

    private fun observePlaceOrder() {
        lifecycleScope.launch {
            billingViewmodel.placeOrder.collect {
                when (it) {
                    is Resources.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    is Resources.Success -> {
                        //navigate to thanks fragment and remove billing fragment from backstack
                        // Navigate to ThanksFragment and clear the backstack
                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(
                                R.id.billingFragment,
                                true
                            ) // Clear BillingFragment from the backstack
                            .build()

                        // Navigate to ThanksFragment with the defined NavOptions
                        findNavController().navigate(
                            R.id.action_billingFragment_to_thanksFragment,
                            null,
                            navOptions
                        )
                    }

                    is Resources.Loading -> {

                    }

                    else -> Unit
                }
            }
        }
    }

    private fun observeAddress() {
        lifecycleScope.launch {
            billingViewmodel.fetchAddress.collect {
                when (it) {
                    is Resources.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    is Resources.Success -> {
                        Log.d("Address", it.data.toString())
                        addressAdapter.differ.submitList(it.data)
                    }

                    is Resources.Loading -> {

                    }

                    else -> Unit
                }
            }
        }
    }


    fun setUpProductRecyclerview() {
        binding.reccylerviewProdcut.apply {
            adapter = billingProductAdapter
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }


    fun setUpAddressRecyclerview() {
        binding.reccylerviewAddress.apply {
            adapter = addressAdapter
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

}