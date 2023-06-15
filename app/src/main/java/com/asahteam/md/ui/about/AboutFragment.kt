package com.asahteam.md.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.asahteam.md.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.let { binding ->
            binding.ourProfileImage1.setOnClickListener {
                openLinkedIn("https://www.linkedin.com/in/fikriiardiansyahh/")
            }
            binding.ourProfileImage2.setOnClickListener {
                openLinkedIn("https://www.linkedin.com/in/zikrikn/")
            }
            binding.ourProfileImage3.setOnClickListener {
                openLinkedIn("https://www.linkedin.com/in/ridwan-ridlo-nugroho-617a10223/")
            }
            binding.ourProfileImage4.setOnClickListener {
                openLinkedIn("https://www.linkedin.com/in/irzanluthfi/")
            }
            binding.ourProfileImage5.setOnClickListener {
                openLinkedIn("https://www.linkedin.com/in/edwin-mahendra/")
            }
            binding.ourProfileImage6.setOnClickListener {
                openLinkedIn("https://www.linkedin.com/in/adiatmaja/")
            }
            binding.ourProfileName1.setOnClickListener {
                openLinkedIn("https://www.linkedin.com/in/fikriiardiansyahh/")
            }
            binding.ourProfileName2.setOnClickListener {
                openLinkedIn("https://www.linkedin.com/in/zikrikn/")
            }
            binding.ourProfileName3.setOnClickListener {
                openLinkedIn("https://www.linkedin.com/in/ridwan-ridlo-nugroho-617a10223/")
            }
            binding.ourProfileName4.setOnClickListener {
                openLinkedIn("https://www.linkedin.com/in/irzanluthfi/")
            }
            binding.ourProfileName5.setOnClickListener {
                openLinkedIn("https://www.linkedin.com/in/edwin-mahendra/")
            }
            binding.ourProfileName6.setOnClickListener {
                openLinkedIn("https://www.linkedin.com/in/adiatmaja/")
            }
        }
    }

    private fun openLinkedIn(link: String) {
        val intent =
            Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}