package com.shahbaz.farming.oboarding.onboardingscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shahbaz.farming.R
import com.shahbaz.farming.databinding.FragmentFourthScreenBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FourthScreen : Fragment() {

    private lateinit var binding:FragmentFourthScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentFourthScreenBinding.inflate(inflater,container,false)
        return binding.root
    }
}