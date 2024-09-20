package com.shahbaz.farming.mainscreen.article

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.shahbaz.farming.R
import com.shahbaz.farming.databinding.FragmentSpecificElementDetailsBinding
import com.shahbaz.farming.datamodel.article.Data


class SpecificElementDetailsFragment : Fragment() {

    private lateinit var binding: FragmentSpecificElementDetailsBinding
    private val args by navArgs<SpecificElementDetailsFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSpecificElementDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = args.articleData
        Log.d("data received", data.toString())
        data?.let {
            showUi(data)
        }
    }

    private fun showUi(data: Data) {
        binding.apply {
            // Load image only if the path is not null or empty
            val imagePath = data.attributes.main_image_path ?: ""
            if (imagePath.isNotEmpty()) {
                Glide.with(requireContext())
                    .load(imagePath)
                    .placeholder(R.drawable.logo_app)
                    .into(imageArticleListFrag)
            }

            // Set text views with default empty strings if values are null
            descTextxArticleListFrag.text = data.attributes.name ?: ""
            description.text = "Description: ${data.attributes.description ?: ""}"
            sunRequirements.text = "Sun Requirements: ${data.attributes.sun_requirements ?: ""}"
            sowingMethod.text = "Sowing Method: ${data.attributes.sowing_method ?: ""}"
            growingDegreeDays.text =
                "Growing Degree Days: ${data.attributes.growing_degree_days ?: ""}"
            binomaialName.text = "Binomial Name: ${data.attributes.binomial_name ?: ""}"
        }
    }


}