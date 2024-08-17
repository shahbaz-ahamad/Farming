package com.shahbaz.farming.oboarding

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.shahbaz.farming.R
import com.shahbaz.farming.databinding.FragmentOnBaordingBinding
import com.shahbaz.farming.oboarding.adapter.OnBoardingAdapter
import com.shahbaz.farming.oboarding.onboardingscreen.FirstScreen
import com.shahbaz.farming.oboarding.onboardingscreen.FourthScreen
import com.shahbaz.farming.oboarding.onboardingscreen.SecondScreen
import com.shahbaz.farming.oboarding.onboardingscreen.ThirdScreen
import com.shahbaz.farming.oboarding.onboardingviewmodel.OnBoardingViewmodel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OnBaordingFragment : Fragment() {

    private lateinit var onBoardingAdapter: OnBoardingAdapter
    private lateinit var binding: FragmentOnBaordingBinding
    private val onBoardingViewmodel by viewModels<OnBoardingViewmodel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnBaordingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        Log.d("tag", onBoardingViewmodel.isOnboardingCompleted().toString())
        val fragmentList = arrayListOf<Fragment>(
            FirstScreen(),
            SecondScreen(),
            ThirdScreen(),
            FourthScreen()
        )
        onBoardingAdapter =
            OnBoardingAdapter(fragmentList, requireActivity().supportFragmentManager, lifecycle)

        binding.viewpager.adapter = onBoardingAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
        }.attach()

        binding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateUI(position)
            }
        })

        binding.buttonPrev.setOnClickListener {
            val currentItem = binding.viewpager.currentItem

            if (currentItem > 0) {
                binding.viewpager.currentItem = currentItem - 1
            }

        }

        binding.buttonNext.setOnClickListener {
            val currentItem = binding.viewpager.currentItem
            val lastPage = onBoardingAdapter.itemCount - 1

            if (currentItem < lastPage) {
                binding.viewpager.currentItem = currentItem + 1
            } else {
                //navigate to login screen
                onBoardingViewmodel.setOnBoardingCompleted(isCompleted = true)
                // Navigate to the authentication graph

                // Navigate to the authentication graph and clear back stack
               navigatToAuthentication()

            }
        }
    }


    private fun updateUI(position: Int) {
        val lastPage = onBoardingAdapter.itemCount - 1

        // Hide "Prev" button on the first page
        binding.buttonPrev.visibility = if (position == 0) View.GONE else View.VISIBLE

        // Change "Next" button text on the last page
        binding.buttonNext.text = if (position == lastPage) "Get Started" else "Next"
    }

    override fun onResume() {
        super.onResume()
        // Hide the status bar
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    override fun onStart() {
        super.onStart()
        if (onBoardingViewmodel.isOnboardingCompleted()) {
            navigatToAuthentication()
        }
    }


    fun navigatToAuthentication(){
        // Navigate to the authentication graph and clear back stack
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.onBaordingFragment, true) // Pop up to the OnBaordingFragment
            .build()

        findNavController().navigate(
            R.id.action_onBaordingFragment_to_authentication_graph,
            null,
            navOptions
        )
    }
}