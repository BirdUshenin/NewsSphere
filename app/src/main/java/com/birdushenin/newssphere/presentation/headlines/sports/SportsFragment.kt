package com.birdushenin.newssphere.presentation.headlines.sports

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.birdushenin.newssphere.MyApplication
import com.birdushenin.newssphere.data.Article
import com.birdushenin.newssphere.databinding.FragmentSportsBinding
import com.birdushenin.newssphere.domain.OnNewsItemClickListener
import com.birdushenin.newssphere.domain.SportNews
import com.birdushenin.newssphere.navigation.Screens
import com.birdushenin.newssphere.presentation.headlines.NewsViewModel
import com.birdushenin.newssphere.presentation.adapters.NewsAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import javax.inject.Inject

class SportsFragment : Fragment() {

    private val adapter = NewsAdapter()
    private val sharedViewModel: NewsViewModel by activityViewModels()
    @Inject
    lateinit var retrofit: Retrofit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSportsBinding.inflate(layoutInflater)
        MyApplication.appComponent.inject(this)

        val newsService = retrofit.create(SportNews::class.java)

        //RecyclerView
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        adapter.setOnUserItemClickListener(object: OnNewsItemClickListener {
            override fun onNewsItemClicked(article: Article) {
                sharedViewModel.selectArticle(article)
                (requireActivity().application as MyApplication).router.navigateTo(Screens.NewsWindowScreen)
            }
        })

        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            binding.progressBar.visibility = View.GONE
            loadNews(newsService)
        }

        return binding.root
    }

    private suspend fun loadNews(newsService: SportNews) {
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