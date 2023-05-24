package com.asahteam.md.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.asahteam.md.R
import com.asahteam.md.databinding.FragmentLoginBinding
import com.asahteam.md.local.data.User
import com.asahteam.md.remote.response.ResultResponse
import com.asahteam.md.ui.utils.AuthViewModelFactory

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModels<LoginViewModel> {
        AuthViewModelFactory.getInstance(
            requireContext()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.let { binding ->
            binding.loginButton.setOnClickListener {
                val username = binding.usernameEt.text.toString().trimIndent()
                val password = binding.passwordEt.text.toString().trimIndent()

                if (username.isEmpty()) {
                    binding.usernameEt.error = "this field can't be empty"
                }

                if (password.isEmpty()) {
                    binding.passwordEt.error = "this field can't  be empty"
                }

                if (username.isNotEmpty() && password.isNotEmpty()) {
                    if (binding.usernameEt.error.isNullOrEmpty() || binding.passwordEt.error.isNullOrEmpty()) {
                        Log.e("loginFragment", username)
                        Log.e("loginFragment", password)
                        viewModel.login(username, password).observe(viewLifecycleOwner) { result ->
                            when (result) {
                                is ResultResponse.Error -> {
                                    binding.progessBar.visibility = View.GONE
                                    Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                                }

                                is ResultResponse.Loading -> {
                                    binding.progessBar.visibility = View.VISIBLE
                                }

                                is ResultResponse.NotFound -> {}

                                is ResultResponse.Success -> {
                                    binding.progessBar.visibility = View.GONE
                                    viewModel.saveUser(
                                        User(
                                            result.data.accessToken,
                                            result.data.refreshToken
                                        )
                                    )
                                    binding.root.findNavController()
                                        .navigate(R.id.action_loginFragment_to_blogFragment)
                                }
                            }
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Silakan isi form dengan benar terlebih dahulu",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            binding.registerButton.setOnClickListener {
                binding.root.findNavController()
                    .navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}