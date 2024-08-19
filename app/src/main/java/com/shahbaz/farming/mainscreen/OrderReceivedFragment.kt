package com.shahbaz.farming.mainscreen

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.shahbaz.farming.R
import com.shahbaz.farming.adapter.OrderReceivedAdapter
import com.shahbaz.farming.databinding.FragmentOrderReceivedBinding
import com.shahbaz.farming.datamodel.Order
import com.shahbaz.farming.util.Resources
import com.shahbaz.farming.viewmodel.OrderViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderReceivedFragment : Fragment() {

    private lateinit var binding: FragmentOrderReceivedBinding
    private val orderViewmodel by viewModels<OrderViewmodel>()
    private val orderAdapterReceivedAdapter by lazy { OrderReceivedAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrderReceivedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerview()

        orderViewmodel.fetchOrderReceived()
        observeOrderReceived()

        orderAdapterReceivedAdapter.onUpdateClick = {
            showUpdateDialogue(it)
        }
        observeUpdateStatus()
    }

    private fun observeUpdateStatus() {
        lifecycleScope.launch {
            orderViewmodel.updateOrderStatus.collect {
                when (it) {
                    is Resources.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Resources.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                    is Resources.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), it.data, Toast.LENGTH_SHORT)
                            .show()
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun showUpdateDialogue(order: Order) {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.update_dropdown, null)
        val spinner = dialogView.findViewById<Spinner>(R.id.dropdown_spinner)
        // Build the dialog
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Select an Option")
            .setView(dialogView)
            .setPositiveButton("OK") { dialogInterface, _ ->
                // Handle OK click, you can retrieve the selected item
                val selectedItem = spinner.selectedItem.toString()
                // Do something with the selected item
                orderViewmodel.updateOrderStatus(order, selectedItem)
                dialogInterface.dismiss()
            }
            .setNegativeButton("Cancel") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()

        dialog.show()

    }

    private fun setupRecyclerview() {
        binding.recyclerViewOrderRecevied.apply {
            adapter = orderAdapterReceivedAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeOrderReceived() {
        lifecycleScope.launch {
            orderViewmodel.fetchYourSelledOrder.collect {
                when (it) {
                    is Resources.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Resources.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), "Failed to get Order", Toast.LENGTH_SHORT)
                            .show()
                    }

                    is Resources.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Log.d("data", it.data.toString())
                        orderAdapterReceivedAdapter.diifer.submitList(it.data)
                    }

                    else -> Unit
                }
            }
        }
    }

}