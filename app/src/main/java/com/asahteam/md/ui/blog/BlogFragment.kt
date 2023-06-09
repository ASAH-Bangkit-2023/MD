package com.asahteam.md.ui.blog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.asahteam.md.R
import com.asahteam.md.databinding.FragmentBlogBinding
import com.asahteam.md.remote.response.ResultResponse
import com.asahteam.md.ui.utils.BlogViewModelFactory

class BlogFragment : Fragment() {
    private var _binding: FragmentBlogBinding? = null
    private val binding get() = _binding
    private val viewModel by viewModels<BlogViewModel> {
        BlogViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBlogBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.let {
            it.scanImageButton.setOnClickListener {
                it.findNavController().navigate(R.id.action_navigation_home_to_scanActivity)
            }
        }

        viewModel.getBlogs().observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultResponse.Error -> {
                    binding?.let {
                        it.progessBar.visibility = View.GONE
                        it.notFoundTv.visibility = View.GONE
                    }
                    Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                }

                is ResultResponse.Loading -> {
                    binding?.let {
                        it.progessBar.visibility = View.VISIBLE
                        it.notFoundTv.visibility = View.GONE
                    }
                }

                is ResultResponse.NotFound -> {
                    binding?.let {
                        it.progessBar.visibility = View.GONE
                        it.notFoundTv.visibility = View.VISIBLE
                    }
                }

                is ResultResponse.Success -> {
                    binding?.let {
                        it.progessBar.visibility = View.GONE
                        it.notFoundTv.visibility = View.GONE
                        it.containerRv.adapter = BlogAdapter(result.data) { blog, view ->
                            val toDetail =
                                BlogFragmentDirections.actionNavigationHomeToBlogDetailFragment()
                            toDetail.id = blog.newsId
                            view.findNavController().navigate(toDetail)
                        }
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