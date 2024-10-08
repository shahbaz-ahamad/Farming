package com.shahbaz.farming.mainscreen

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.messaging.FirebaseMessaging
import com.shahbaz.farming.R
import com.shahbaz.farming.adapter.ArticleAdapter
import com.shahbaz.farming.databinding.FragmentHomeBinding
import com.shahbaz.farming.datamodel.Article
import com.shahbaz.farming.datamodel.weahterdatamodel.WeatherRootList
import com.shahbaz.farming.permission.checkPermission
import com.shahbaz.farming.permmsion.checkImagePermissionForNotifcation
import com.shahbaz.farming.repo.apmc.APMCRepo
import com.shahbaz.farming.util.Constant
import com.shahbaz.farming.util.Constant.Companion.LOCATION_PERMISSION_REQUEST_CODE
import com.shahbaz.farming.util.Constant.Companion.articleList
import com.shahbaz.farming.util.Resources
import com.shahbaz.farming.util.showBottomNavigationBar
import com.shahbaz.farming.viewmodel.BillingViewmodel
import com.shahbaz.farming.viewmodel.article.ArticleViewModel
import com.shahbaz.farming.weather.WeatherViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val weatherViewmodel by viewModels<WeatherViewmodel>()
    private val billingViewmodle by viewModels<BillingViewmodel>()
    private val articleAdapter by lazy {
        ArticleAdapter()
    }

    //wee have to add dependency for this
    private lateinit var fuesdLocationClient: FusedLocationProviderClient

    private var lat: String? = null
    private var lon: String? = null

    @Inject
    lateinit var priceRepo: APMCRepo

    val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            results.entries.forEach {
                if (it.value) {
                    Toast.makeText(requireContext(), "${it.key} granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "${it.key} denied", Toast.LENGTH_SHORT).show()
                }
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
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUprecyclerview()
        priceRepo.fetchApmcData()


        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            billingViewmodle.updateFCMtoken(it)
            Log.d("Token", it)
        }


        com.shahbaz.farming.permmsion.checkImagePermissionForPhoto(
            requireContext(),
            requestPermissions
        )

        checkImagePermissionForNotifcation(requireContext(), requestPermissions)
        fetchLocation()
        observeWeather()


        articleAdapter.onItemClick = {
            Log.d("article", it)
            val bundle = Bundle().apply {
                putString("name", it)
            }
            findNavController().navigate(R.id.action_homeFragment_to_articleDetailsFragment, bundle)
        }

        binding.predicthealthImage.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_healthPredectionFragment)
        }

        binding.chatWithBot.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_chatFragment)
        }

    }


    private fun observeWeather() {
        lifecycleScope.launch {
            weatherViewmodel.weatherState.collect {
                when (it) {
                    is Resources.Error -> {
                        Log.d("Api Response", it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    is Resources.Loading -> {
                        binding.weatherinfo.visibility = View.GONE
                    }

                    is Resources.Success -> {
                        binding.weatherinfo.visibility = View.VISIBLE
                        Log.d("Api Response", it.data.toString())
                        it.data?.let { data ->
                            updateWeatherUI(data)
                        }
                    }

                    is Resources.Unspecified -> {
                    }
                }
            }
        }
    }

    private fun updateWeatherUI(data: WeatherRootList) {
        binding.weatherWind.text = "Wind: ${data.wind.speed} km/hr"
        binding.weatherHumidity.text = "Humidity: ${data.main.humidity} %"
        val iconcode = data.weather[0].icon
        var iconurl = "https://openweathermap.org/img/w/" + iconcode + ".png"
        Log.d("icon", iconurl)
        Glide.with(requireContext()).load(iconurl).into(binding.weatherIconImage)
        binding.cityName.text = data.name
        weatherViewmodel.setCityName(data.name)
        binding.weatherTemperature.text = "${(data.main.temp - 273).toInt()}" + "\u2103"
    }


    private fun fetchLocation() {
        if (checkPermission(requireContext())) {
            fuesdLocationClient =
                LocationServices.getFusedLocationProviderClient(requireContext())
            fuesdLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    lat = it.latitude.toString()
                    lon = it.longitude.toString()
                    weatherViewmodel.getWeather(lat!!, lon!!)
                    Log.d("lat", lat.toString())
                    Log.d("lon", lon.toString())
                } ?: run {
                    Toast.makeText(requireContext(), "Failed to get location", Toast.LENGTH_SHORT)
                        .show()
                }
            }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to get location", Toast.LENGTH_SHORT)
                        .show()
                }
        } else {
            // Request permission if not already granted
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationBar()
    }

    fun setUprecyclerview() {
        binding.recyclerviewArticle.apply {
            adapter = articleAdapter
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            articleAdapter.differ.submitList(articleList)
        }
    }


}
