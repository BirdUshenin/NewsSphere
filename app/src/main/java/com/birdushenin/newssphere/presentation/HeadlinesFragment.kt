package com.birdushenin.newssphere.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.birdushenin.newssphere.Application
import com.birdushenin.newssphere.R
import com.birdushenin.newssphere.data.Article
import com.birdushenin.newssphere.databinding.FragmentHeadlinesBinding
import com.birdushenin.newssphere.domain.NewsService
import com.birdushenin.newssphere.domain.OnNewsItemClickListener
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class HeadlinesFragment : Fragment() {

    private val adapter = NewsAdapter()

    @Inject
    lateinit var retrofit: Retrofit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHeadlinesBinding.inflate(layoutInflater)
        Application.appComponent.inject(this)

        val newsService = retrofit.create(NewsService::class.java)

        //RecyclerView
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        adapter.setOnUserItemClickListener(object: OnNewsItemClickListener {
            override fun onNewsItemClicked(article: Article) {
                val fragmentWindow = NewsWindowFragment.newInstance(
                    article.title,
                    article.description,
                    article.urlToImage,
                    article.source.name
                )

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_window, fragmentWindow)
                    .addToBackStack("FragWindow")
                    .commit()
            }
        })

        binding.progressBar.visibility = View.VISIBLE

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
                    adapter.submitList(newsList)
                }
            }
        } catch (_: Exception) { }
    }
}