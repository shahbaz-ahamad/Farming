package com.shahbaz.farming.oboarding.onboardingscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shahbaz.farming.R
import com.shahbaz.farming.databinding.FragmentSecondScreenBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SecondScreen : Fragment() {


    private lateinit var binding: FragmentSecondScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSecondScreenBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

}