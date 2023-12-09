package com.birdushenin.newssphere.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.birdushenin.newssphere.Application
import com.birdushenin.newssphere.R
import com.birdushenin.newssphere.data.Article
import com.birdushenin.newssphere.databinding.FragmentNewsWindowBinding
import com.birdushenin.newssphere.domain.NewsService
import com.birdushenin.newssphere.domain.OnNewsItemClickListener
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import javax.inject.Inject

class NewsWindowFragment : Fragment() {

    private lateinit var binding: FragmentNewsWindowBinding
    @Inject
    lateinit var retrofit: Retrofit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsWindowBinding.inflate(inflater)

        Application.appComponent.inject(this)
        val newsService = retrofit.create(NewsService::class.java)

        lifecycleScope.launch {
            binding.progressBar.visibility = View.GONE
            loadNews(newsService)
        }
        return binding.root
    }

    private suspend fun loadNews(newsService: NewsService) {
        val apiKey = "eae4e313c2d043c183e78149bc172501"

        try {
            val response = newsService.getTopHeadlines(apiKey = apiKey)
            if (response.isSuccessful) {
                val newsList = response.body()?.articles ?: emptyList()
                withContext(Dispatchers.Main) {
                    if (newsList.isNotEmpty()) {
                        val firstArticle = newsList[0]
                        binding.mainText.text = firstArticle.title
                        binding.description.text = firstArticle.description
                        binding.data.text = firstArticle.content
                        binding.source.text = firstArticle.source.name

                        Glide.with(binding.picNews)
                            .load(firstArticle.urlToImage)
                            .into(binding.picNews)
                    }
                }
            }
        } catch (_: Exception) { }
    }
}