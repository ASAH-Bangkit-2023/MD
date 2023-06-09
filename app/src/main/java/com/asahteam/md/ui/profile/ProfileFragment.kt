package com.asahteam.md.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.asahteam.md.R
import com.asahteam.md.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding

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
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}