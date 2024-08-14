package com.shahbaz.farming.mainscreen

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.shahbaz.farming.R
import com.shahbaz.farming.databinding.FragmentAddPostBinding
import com.shahbaz.farming.util.Resources
import com.shahbaz.farming.util.hideBottomNavigationBar
import com.shahbaz.farming.util.progressDialgoue
import com.shahbaz.farming.viewmodel.AddPostViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddPostFragment : Fragment() {

    private lateinit var binding: FragmentAddPostBinding
    var imageUrl: String = ""
    private lateinit var progressDialog: ProgressDialog
    private val addPostViewmodel by viewModels<AddPostViewmodel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = ProgressDialog(requireContext())

        binding.addImage.setOnClickListener {
            imageSelectorLauncher.launch("image/*")
        }

        binding.createPost.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val description = binding.etDescription.text.toString().trim()
            if (title != "" && description != "" && imageUrl != "") {

                progressDialgoue(
                    progressDialog,
                    "Uploading Post...",
                    "Upload In Progress..."
                )
                addPostViewmodel.addPost(
                    title = title,
                    description = description,
                    imageUrl = imageUrl
                )
            } else {
                //show toast
                Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        observeAddPost()
    }

    private fun observeAddPost() {
        lifecycleScope.launch {
            addPostViewmodel.postStatus.collect {
                when (it) {
                    is Resources.Error -> {
                        progressDialog.hide()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        progressDialog.hide()
                        Toast.makeText(
                            requireContext(),
                            "Post Uploaded Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(R.id.postFragment2, inclusive = true)
                            .build()

                        findNavController().navigate(R.id.action_addPostFragment_to_postFragment2, null, navOptions)
                        // Reset the ViewModel state to avoid the toast when returning to AddPostFragment
                        addPostViewmodel.resetPostStatus()


                    }

                    is Resources.Unspecified -> {}
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        hideBottomNavigationBar()
    }

    private val imageSelectorLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        imageUrl = uri.toString()
        Glide.with(requireContext()).load(imageUrl).into(binding.addImage)
    }


}