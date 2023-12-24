package com.birdushenin.newssphere.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.birdushenin.newssphere.MyApplication
import com.birdushenin.newssphere.data.Article
import com.birdushenin.newssphere.databinding.FragmentBusinessBinding
import com.birdushenin.newssphere.domain.NewsService
import com.birdushenin.newssphere.domain.OnNewsItemClickListener
import com.birdushenin.newssphere.navigation.Screens
import com.birdushenin.newssphere.presentation.adapters.NewsAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import javax.inject.Inject

class BusinessFragment : Fragment() {

    private lateinit var binding: FragmentBusinessBinding

    private val adapter = NewsAdapter()
    private val sharedViewModel: NewsViewModel by activityViewModels()
    @Inject
    lateinit var retrofit: Retrofit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentBusinessBinding.inflate(layoutInflater)
        MyApplication.appComponent.inject(this)

        val newsService = retrofit.create(NewsService::class.java)

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

            loadNews(newsService, q = "Elon")
        }

        return binding.root
    }

    private suspend fun loadNews(newsService: NewsService, q: String) {
        val apiKey = "eae4e313c2d043c183e78149bc172501"

        try {

            val response = newsService.getRelevant(apiKey = apiKey, query = q)
            if (response.isSuccessful) {
                val newsList = response.body()?.articles ?: emptyList()
                withContext(Dispatchers.Main) {
                    adapter.submitList(newsList)
                    binding.progressBar.visibility = View.GONE
                }
            }
        } catch (_: Exception) { }
    }
}