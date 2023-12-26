package com.birdushenin.newssphere.presentation.adapters

import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import com.birdushenin.newssphere.data.Article
import com.bumptech.glide.Glide
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.birdushenin.newssphere.R
import com.birdushenin.newssphere.databinding.ItemArticleBinding
import com.birdushenin.newssphere.domain.OnNewsItemClickListener

class NewsAdapter : ListAdapter<Article, NewsAdapter.ArticleViewHolder>(DIFF_CALLBACK) {

    private var clickListener: OnNewsItemClickListener? = null

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemArticleBinding.inflate(inflater, parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article)
        holder.itemView.setOnClickListener {
            clickListener?.onNewsItemClicked(article)
        }
    }

    fun setOnUserItemClickListener(listener: OnNewsItemClickListener) {
        clickListener = listener
    }

    inner class ArticleViewHolder(private val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) {
            binding.title.text = article.title
            binding.description.text = article.description ?: ""
            binding.source.text = article.source.name
            binding.publishedAt.text = article.publishedAt
            binding.content.text = article.content

            binding.sourceView.setImageResource(article.getSourceDrawable())
            Glide.with(binding.image)
                .load(article.urlToImage)
                .into(binding.image)
        }
    }
}