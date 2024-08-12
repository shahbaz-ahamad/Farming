package com.shahbaz.farming.authentication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.shahbaz.farming.DashboardActivity
import com.shahbaz.farming.R
import com.shahbaz.farming.databinding.FragmentLoginBinding
import com.shahbaz.farming.util.Resources
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {


    private lateinit var binding: FragmentLoginBinding
    private val authenticationViewmodel by viewModels<AuthenticationViewmodel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val email = it.getString("email")
            val password = it.getString("password")
            binding.Emailorphone.setText(email)
            binding.Loginpassword.setText(password)
        }

        binding.button.setOnClickListener {
            binding.apply {
                val email = Emailorphone.text.toString().trim()
                val pasword = Loginpassword.text.toString().trim()
                //"shahbaz1@gmail.com","HeyThis1234"

                if (email.isEmpty() || pasword.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Please fill all the fields",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                } else {
                    binding.progressBar.visibility = View.VISIBLE
                    authenticationViewmodel.signInWithEmailAndPassword(email, pasword)
                }

            }
        }

        binding.donthaveanaccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        observeLoginProcess()
    }

    private fun observeLoginProcess() {
        lifecycleScope.launch {
            authenticationViewmodel.loginState.collect {
                when (it) {
                    is Resources.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    is Resources.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Resources.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), it.data, Toast.LENGTH_SHORT).show()
                        goToDashboardActivity()
                    }

                    is Resources.Unspecified -> {}
                }
            }
        }


    }


    override fun onStart() {
        super.onStart()

        if (authenticationViewmodel.isLoggedIn()) {
            goToDashboardActivity()
        }
    }

    fun goToDashboardActivity() {
        val intent = Intent(requireContext(), DashboardActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}