package com.shahbaz.farming.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.shahbaz.farming.R
import com.shahbaz.farming.databinding.FragmentSignUpBinding
import com.shahbaz.farming.util.Resources
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private val authenticationViewmodel by viewModels<AuthenticationViewmodel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authenticationViewmodel.createUser("shahbaz1@gmail.com", "HeyThis1234","Shahbaz")
        observeAuthentication()
    }

    private fun observeAuthentication() {
        lifecycleScope.launch {
            authenticationViewmodel.authenticationState.collect {
                when (it) {
                    is Resources.Loading -> {

                    }

                    is Resources.Error -> {
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                    }

                    is Resources.Success -> {
                        Toast.makeText(requireContext(),"Login Succesfull",Toast.LENGTH_SHORT).show()

                    }

                    is Resources.Unspecified -> {

                    }
                }
            }
        }
    }
}