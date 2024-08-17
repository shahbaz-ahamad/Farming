package com.shahbaz.farming.mainscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.shahbaz.farming.R
import com.shahbaz.farming.adapter.YourProductAdaapter
import com.shahbaz.farming.databinding.FragmentYourProductBinding
import com.shahbaz.farming.util.Resources
import com.shahbaz.farming.util.hideBottomNavigationBar
import com.shahbaz.farming.viewmodel.AddProductViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class YourProductFragment : Fragment() {

    private lateinit var binding: FragmentYourProductBinding
    private val addProductViewmodel by viewModels<AddProductViewmodel>()
    private val yourProductAdaapter by lazy {
        YourProductAdaapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentYourProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpProductRecyclerView()
        addProductViewmodel.fetchYourProduct()
        observeYourProduct()

    }

    private fun observeYourProduct() {
        lifecycleScope.launch {
            addProductViewmodel.yourProductStatus.collect {
                when (it) {
                    is Resources.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        it.data?.let { product ->
                            yourProductAdaapter.differ.submitList(product)
                        }
                    }

                    is Resources.Unspecified -> {}
                }
            }
        }
    }

    private fun setUpProductRecyclerView() {
        binding.yourProductRecyclerview.apply {
            adapter = yourProductAdaapter
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