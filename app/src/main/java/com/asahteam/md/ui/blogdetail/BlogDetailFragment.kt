package com.asahteam.md.ui.blogdetail

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.asahteam.md.databinding.FragmentBlogDetailBinding
import com.asahteam.md.remote.response.ResultResponse
import com.asahteam.md.ui.utils.BlogViewModelFactory
import com.bumptech.glide.Glide

class BlogDetailFragment : Fragment() {
    private var _binding: FragmentBlogDetailBinding? = null
    private val binding get() = _binding
    private val viewModel by viewModels<BlogDetailViewModel> {
        BlogViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBlogDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = BlogDetailFragmentArgs.fromBundle(arguments as Bundle).id

        if (id != -1) {
            viewModel.getBlog(id).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is ResultResponse.Error -> {
                        binding?.let {
                            it.progessBar.visibility = View.GONE
                            it.blocker.visibility = View.GONE
                        }
                        Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                    }

                    is ResultResponse.Loading -> {
                        binding?.let {
                            it.progessBar.visibility = View.VISIBLE
                            it.blocker.visibility = View.VISIBLE
                        }
                    }

                    is ResultResponse.NotFound -> {}
                    is ResultResponse.Success -> {
                        binding?.let {
                            it.apply {
                                progessBar.visibility = View.GONE
                                it.blocker.visibility = View.GONE
                                title.text = result.data.title
                                informationAuthor.text = result.data.author
                                informationDate.text = result.data.dateNews
                                description.text =
                                    Html.fromHtml(result.data.content, Html.FROM_HTML_MODE_COMPACT)
                                Glide.with(requireContext()).load(result.data.thumbnail)
                                    .into(it.thumbnailImage)
                            }
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