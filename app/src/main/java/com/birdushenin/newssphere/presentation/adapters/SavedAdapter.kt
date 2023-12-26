package com.birdushenin.newssphere.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.birdushenin.newssphere.data.SavedClass
import com.birdushenin.newssphere.databinding.ItemSavedBinding
import com.birdushenin.newssphere.domain.OnSavedItemClickListener
import com.bumptech.glide.Glide

private var clickListener: OnSavedItemClickListener? = null

class SavedAdapter : ListAdapter<SavedClass, SavedAdapter.SavedViewHolder> (DIFF_CALLBACK) {
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SavedClass>() {
            override fun areItemsTheSame(oldItem: SavedClass, newItem: SavedClass): Boolean {
                return oldItem.urlText == newItem.urlText
            }

            override fun areContentsTheSame(oldItem: SavedClass, newItem: SavedClass): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedAdapter.SavedViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSavedBinding.inflate(inflater, parent, false)
        return SavedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedAdapter.SavedViewHolder, position: Int) {
        val savedClass = getItem(position)
        holder.bind(savedClass)
        holder.itemView.setOnClickListener {
            clickListener?.onSavedItemClicked(savedClass)
        }
    }

    fun setOnUserItemClickListener(listener: OnSavedItemClickListener) {
        clickListener = listener
    }

    inner class SavedViewHolder(private val binding: ItemSavedBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(savedClass: SavedClass) {
            binding.titleText.text = savedClass.titleText
            binding.descriptionText.text = savedClass.descriptionText ?: ""
            binding.sourceText.text = savedClass.sourceText
            binding.publishedAt.text = savedClass.publishedAt
            binding.sourceView.setImageResource(savedClass.getSourceDrawable())

            Glide.with(binding.image)
                .load(savedClass.imagePic)
                .into(binding.image)
        }
    }
}