package com.shahbaz.farming.mainscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.shahbaz.farming.R
import com.shahbaz.farming.databinding.FragmentAddressBinding
import com.shahbaz.farming.datamodel.Address
import com.shahbaz.farming.util.Resources
import com.shahbaz.farming.viewmodel.BillingViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddressFragment : Fragment() {

    private lateinit var binding: FragmentAddressBinding
    private val billingViewmodel by viewModels<BillingViewmodel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSave.setOnClickListener {
            //fetch all the data of the edit text
            val addressTitle = binding.edAddressTitle.text.toString().trim()
            val fullName = binding.edFullName.text.toString().trim()
            val street = binding.edStreet.text.toString().trim()
            val phone = binding.edPhone.text.toString().trim()
            val city = binding.edCity.text.toString().trim()
            val state = binding.edState.text.toString().trim()

            //check if everything is not empty
            if (addressTitle.isNotEmpty() && fullName.isNotEmpty() && street.isNotEmpty() && phone.isNotEmpty() && city.isNotEmpty() && state.isNotEmpty()) {
                val address = Address(
                    addressTitle,
                    fullName,
                    street,
                    phone,
                    city,
                    state
                )
                billingViewmodel.addAddress(address)
            } else {
                Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        observeAddressState()

    }

    private fun observeAddressState() {
        lifecycleScope.launch {
            billingViewmodel.addressStatus.collect {
                when (it) {
                    is Resources.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    is Resources.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Resources.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Address Added Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        billingViewmodel.resetAddressStatus()
                    }

                    else -> Unit

                }
            }
        }
    }

}