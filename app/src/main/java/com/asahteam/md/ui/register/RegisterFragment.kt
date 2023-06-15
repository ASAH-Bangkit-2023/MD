package com.asahteam.md.ui.register

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.asahteam.md.R
import com.asahteam.md.databinding.FragmentRegisterBinding
import com.asahteam.md.remote.response.ResultResponse
import com.asahteam.md.ui.utils.AuthViewModelFactory

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding
    private val viewModel by viewModels<RegisterViewModel> {
        AuthViewModelFactory.getInstance(
            requireContext()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.let { binding ->
            binding.confirmPasswordEt.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard()
                    true
                } else {
                    false
                }
            }

            binding.registerButton.setOnClickListener {
                hideKeyboard()
                val username = binding.usernameEt.text.toString().trimIndent()
                val password = binding.passwordEt.text.toString().trimIndent()
                val confirmPassword = binding.confirmPasswordEt.text.toString().trimIndent()
                val email = binding.emailEt.text.toString().trimIndent()
                val fullName = binding.fullNameEt.text.toString().trimIndent()

                if (fullName.isEmpty()) {
                    binding.fullNameEt.error = "this field can't be empty"
                }

                if (email.isEmpty()) {
                    binding.emailEt.error = "this field can't be empty"
                }

                if (username.isEmpty()) {
                    binding.usernameEt.error = "this field can't be empty"
                }

                if (password.isEmpty()) {
                    binding.passwordEt.error = "this field can't be empty"
                }

                if (confirmPassword.isEmpty()) {
                    binding.confirmPasswordEt.error = "this field can't be empty"
                }

                if (confirmPassword != password) {
                    binding.confirmPasswordEt.error = "confirm password is not identical"
                }

                if (email.isNotEmpty() && fullName.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && confirmPassword == password) {
                    if (binding.emailEt.error.isNullOrEmpty() || binding.fullNameEt.error.isNullOrEmpty() || binding.usernameEt.error.isNullOrEmpty() || binding.passwordEt.error.isNullOrEmpty() || binding.confirmPasswordEt.error.isNullOrEmpty()) {
                        viewModel.register(
                            username,
                            password,
                            email,
                            fullName
                        ).observe(viewLifecycleOwner) { result ->
                            when (result) {
                                is ResultResponse.Error -> {
                                    binding.progessBar.visibility = View.GONE
                                    binding.blocker.visibility = View.GONE
                                    Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                                }

                                is ResultResponse.Loading -> {
                                    binding.progessBar.visibility = View.VISIBLE
                                    binding.blocker.visibility = View.VISIBLE
                                }

                                is ResultResponse.NotFound -> {}

                                is ResultResponse.Success -> {
                                    binding.progessBar.visibility = View.GONE
                                    binding.blocker.visibility = View.GONE
                                    binding.root.findNavController()
                                        .navigate(R.id.action_registerFragment_to_loginFragment)
                                }
                            }
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "silakan isi form dengan  benar terlebih  dahulu !",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            binding.loginButton.setOnClickListener {
                hideKeyboard()
                binding.root.findNavController()
                    .navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }
    }

    private fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}