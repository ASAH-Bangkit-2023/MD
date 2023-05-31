package com.asahteam.md.ui.blog

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.asahteam.md.databinding.BlogAdapterBinding
import com.asahteam.md.remote.response.BlogResponse
import com.bumptech.glide.Glide

class BlogAdapter(
    private val blogs: List<BlogResponse>,
    private val onClick: (blog: BlogResponse, view: View) -> Unit
) :
    RecyclerView.Adapter<BlogAdapter.ViewHolder>() {
    class ViewHolder(val binding: BlogAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BlogAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return blogs.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val blog = blogs[position]

        holder.binding.title.text = blog.title
        holder.binding.description.text = Html.fromHtml(blog.content, Html.FROM_HTML_MODE_COMPACT)

        Glide.with(holder.itemView.context).load(blog.thumbnail).into(holder.binding.thumbnailImage)

        holder.binding.containerBlog.setOnClickListener {
            onClick(blog, it)
        }
    }
}