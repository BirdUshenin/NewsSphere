package com.birdushenin.newssphere.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.birdushenin.newssphere.data.SourceNews
import com.birdushenin.newssphere.databinding.ItemSourceBinding
import com.birdushenin.newssphere.domain.OnSourceItemClickListener
import com.bumptech.glide.Glide

class SourceAdapter : ListAdapter<SourceNews, SourceAdapter.SourceViewHolder>(DIFF_CALLBACK) {

    private var clickListener: OnSourceItemClickListener? = null

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SourceNews>() {
            override fun areItemsTheSame(oldItem:SourceNews, newItem: SourceNews): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: SourceNews, newItem: SourceNews): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceAdapter.SourceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSourceBinding.inflate(inflater, parent, false)
        return SourceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SourceAdapter.SourceViewHolder, position: Int) {
        val sourceNews = getItem(position)
        holder.bind(sourceNews)
        holder.itemView.setOnClickListener {
            clickListener?.onNewsItemClicked(sourceNews)
        }
    }

    fun setOnUserItemClickListener(listener: OnSourceItemClickListener) {
        clickListener = listener
    }

    inner class SourceViewHolder(private val binding: ItemSourceBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(source: SourceNews) {
            binding.name.text = source.name
            binding.country.text = source.country

            binding.category.text = source.category
            binding.description.text = source.description ?: ""
            binding.image.setImageResource(source.getSourceDrawable())
//            Glide.with(binding.image)
//                .load(source.urlToImage)
//                .into(binding.image)
        }
    }

}