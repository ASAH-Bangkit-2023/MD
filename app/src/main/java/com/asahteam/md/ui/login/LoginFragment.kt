package com.asahteam.md.ui.login

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.asahteam.md.R
import com.asahteam.md.databinding.FragmentLoginBinding
import com.asahteam.md.local.data.User
import com.asahteam.md.remote.response.ResultResponse
import com.asahteam.md.ui.utils.AuthViewModelFactory
import java.time.LocalDate

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModels<LoginViewModel> {
        AuthViewModelFactory.getInstance(
            requireContext()
        )
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            requireActivity().finish()
        }
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
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
        binding?.let { binding ->
            binding.passwordEt.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard()
                    true
                } else {
                    false
                }
            }
            binding.loginButton.setOnClickListener {
                hideKeyboard()
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
                        viewModel.login(username, password).observe(viewLifecycleOwner) { result ->
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
                                    viewModel.saveUser(
                                        User(
                                            result.data.accessToken,
                                            result.data.refreshToken,
                                            LocalDate.now().dayOfMonth
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
                hideKeyboard()
                binding.root.findNavController()
                    .navigate(R.id.action_loginFragment_to_registerFragment)
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
        onBackPressedCallback.remove()
    }
}