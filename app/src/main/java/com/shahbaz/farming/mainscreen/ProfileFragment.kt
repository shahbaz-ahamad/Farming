package com.shahbaz.farming.mainscreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.shahbaz.farming.R
import com.shahbaz.farming.databinding.FragmentProfileBinding
import com.shahbaz.farming.util.Resources
import com.shahbaz.farming.util.hideBottomNavigationBar
import com.shahbaz.farming.viewmodel.HomeFragmentViewmodel
import com.shahbaz.farming.viewmodel.OrderViewmodel
import dagger.hilt.android.AndroidEntryPoint
import ir.mahozad.android.PieChart
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val profileViewmodel by viewModels<HomeFragmentViewmodel>()
    private val orderViewmodel by viewModels<OrderViewmodel>()
    private var coverPhotoUrl: String = ""

    val imageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->

        uri?.let { it ->
            coverPhotoUrl = it.toString()
            profileViewmodel.uploadCoverPhoto(it.toString())
            Glide.with(requireContext()).load(it.toString()).into(binding.userBackgroundImage)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewmodel.getCurrentUserDetails()
        observeUserDetails()
        observeCoverUpload()
        binding.userBackgroundImage.setOnClickListener {
            imageLauncher.launch("image/*")
        }

        orderViewmodel.getOrderReceived()
        orderViewmodel.getOrderPlacedCount()

        observeOrderCount()


    }

    private fun observeOrderCount() {
        lifecycleScope.launch {
            orderViewmodel.orderPlaced.collect { orderPlaced ->
                when (orderPlaced) {

                    is Resources.Error -> {

                    }

                    is Resources.Loading -> {

                    }

                    is Resources.Success -> {
                        orderViewmodel.orderReceived.collect { orderReceived ->
                            when (orderReceived) {
                                is Resources.Error -> {

                                }

                                is Resources.Loading -> {

                                }

                                is Resources.Success -> {

                                    val orderplacedCount = orderPlaced.data
                                    val ordersold = orderReceived.data
                                    if (orderplacedCount != null && ordersold != null) {
                                        updatePieChart(orderplacedCount,ordersold)
                                    }
                                }

                                else -> Unit
                            }
                        }
                    }

                    else -> Unit
                }
            }

        }
    }

    private fun observeCoverUpload() {
        lifecycleScope.launch {
            profileViewmodel.updateCoverPictureStatus.collect {
                when (it) {

                    is Resources.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        binding.uploadBackProgressCover.visibility = View.GONE
                    }

                    is Resources.Loading -> {
                        binding.uploadBackProgressCover.visibility = View.VISIBLE
                    }

                    is Resources.Success -> {
                        binding.uploadUserBackgroundImage.visibility = View.GONE
                        binding.uploadBackProgressCover.visibility = View.GONE
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun observeUserDetails() {
        lifecycleScope.launch {
            profileViewmodel.userDetailState.collect {
                when (it) {

                    is Resources.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        binding.uploadProgressBarProfile.visibility = View.GONE
                    }

                    is Resources.Loading -> {
                        binding.uploadProgressBarProfile.visibility = View.VISIBLE
                        binding.uploadUserBackgroundImage.visibility = View.VISIBLE
                    }

                    is Resources.Success -> {
                        binding.uploadProgressBarProfile.visibility = View.GONE
                        binding.userNameUserProfileFrag.text = it.data?.name
                        binding.userEmailUserProfileFrag.text = it.data?.email

                        if (it.data?.profileUrl != null) {
                            Glide.with(requireContext()).load(it.data?.profileUrl)
                                .into(binding.userImageUserFrag)
                        }

                        if (it.data?.coverPhotoUrl != null) {
                            binding.uploadUserBackgroundImage.visibility = View.GONE
                            Glide.with(requireContext()).load(it.data.coverPhotoUrl)
                                .into(binding.userBackgroundImage)
                        }
                    }

                    else -> {

                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        hideBottomNavigationBar()
    }

    private fun updatePieChart(placedCount: Int, soldCount: Int) {
        Log.d("ccc", placedCount.toString())
        Log.d("sss", soldCount.toString())
        val totalCount = placedCount + soldCount
        val placedSlice = if (totalCount > 0) placedCount.toFloat() / totalCount else 0f
        val soldSlice = if (totalCount > 0) soldCount.toFloat() / totalCount else 0f

        binding.pieChart.slices = listOf(
            PieChart.Slice(placedSlice, resources.getColor(R.color.green)),
            PieChart.Slice(soldSlice, resources.getColor(R.color.grey))
        )
    }
}