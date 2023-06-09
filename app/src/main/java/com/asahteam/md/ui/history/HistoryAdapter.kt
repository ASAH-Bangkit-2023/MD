package com.asahteam.md.ui.history

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.asahteam.md.databinding.BlogAdapterBinding
import com.asahteam.md.databinding.HistoryAdapterBinding
import com.asahteam.md.remote.response.ScanResponse
import com.asahteam.md.ui.blog.BlogAdapter
import com.bumptech.glide.Glide

class HistoryAdapter(
    private val histories: List<ScanResponse>
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    class ViewHolder(val binding: HistoryAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HistoryAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryAdapter.ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return histories.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = histories[position]

        holder.binding.predictionWaste.text = history.predictionWaste
        holder.binding.messageDescription.text = history.message
        holder.binding.dateWaste.text = history.dateScan
    }

}