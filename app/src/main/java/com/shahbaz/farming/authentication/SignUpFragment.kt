package com.shahbaz.farming.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.shahbaz.farming.R
import com.shahbaz.farming.databinding.FragmentSignUpBinding
import com.shahbaz.farming.util.Resources
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private val authenticationViewmodel by viewModels<AuthenticationViewmodel>()
    val email:String?=null
    val password:String?=null

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

        binding.button.setOnClickListener {
            binding.apply {
                val name = Name.text.toString().trim()
                val email = Emailorphone.text.toString().trim()
                val password = Loginpassword.text.toString().trim()
                val confirmPassword = cnfpassword.text.toString().trim()

                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }else if(password != confirmPassword){
                    Toast.makeText(requireContext(), "Password does not match", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }else{
                    binding.progressBar.visibility = View.VISIBLE

                    authenticationViewmodel.createUser(email,password,name)
                }
            }
        }

        binding.alreadyHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
        observeAuthentication()
    }

    private fun observeAuthentication() {
        lifecycleScope.launch {
            authenticationViewmodel.authenticationState.collect {
                when (it) {
                    is Resources.Loading -> {
                        binding.progressBar.visibility=View.VISIBLE
                    }

                    is Resources.Error -> {
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility=View.GONE
                    }

                    is Resources.Success -> {
                        val data = Bundle().apply {
                            putString("email",email)
                            putString("password",password)
                        }
                        Toast.makeText(requireContext(),"Signup Successfully",Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility=View.GONE
                        findNavController().navigate(R.id.action_signUpFragment_to_loginFragment,data)
                    }

                    is Resources.Unspecified -> {

                    }

                    else -> {}
                }
            }
        }
    }
}