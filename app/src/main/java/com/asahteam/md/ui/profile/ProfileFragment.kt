package com.asahteam.md.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.asahteam.md.R
import com.asahteam.md.databinding.FragmentProfileBinding
import com.asahteam.md.remote.response.ResultResponse
import com.asahteam.md.ui.utils.ProfileViewModelFactory

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding
    private val viewModel by viewModels<ProfileViewModel> {
        ProfileViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.let {
            it.history.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_profile_to_historyActivity)
            }
            it.ingatBuangSampah.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_profile_to_reminderActivity)
            }
            it.tukarPoint.setOnClickListener {
                findNavController().navigate((R.id.action_navigation_profile_to_rewardActivity))
            }
        }
        viewModel.getPoint().observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultResponse.Error -> {
                    binding?.let {
                        it.progessBar.visibility = View.GONE
                    }
                    Toast.makeText(context, result.error, Toast.LENGTH_LONG).show()
                }

                ResultResponse.Loading -> {
                    binding?.let {
                        it.progessBar.visibility = View.VISIBLE
                    }
                }

                ResultResponse.NotFound -> {

                }

                is ResultResponse.Success -> {
                    binding?.let {
                        it.progessBar.visibility = View.GONE
                        it.profileName.text = result.data.username
                        it.profilePoint.text = result.data.totalPoints.toString()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}