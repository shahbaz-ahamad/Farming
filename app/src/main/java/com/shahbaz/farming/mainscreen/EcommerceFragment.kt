package com.shahbaz.farming.mainscreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.shahbaz.farming.R
import com.shahbaz.farming.adapter.CatgoryApdater
import com.shahbaz.farming.databinding.FragmentEcommerceBinding
import com.shahbaz.farming.util.Constant.Companion.CATEGORY_LIST
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EcommerceFragment : Fragment() {


    private lateinit var binding: FragmentEcommerceBinding
    private val categoryAdapter by lazy { CatgoryApdater(CATEGORY_LIST) }
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

        setUpCategoryRecyclerview()
        categoryAdapter.selectedPosition = 0
        categoryAdapter.onCategoryClick = {
            Log.d("category", it.toString())
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
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