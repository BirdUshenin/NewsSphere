package com.birdushenin.newssphere.presentation.sources

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.birdushenin.newssphere.MyApplication
import com.birdushenin.newssphere.data.SourceNews
import com.birdushenin.newssphere.data.databases.daos.SourceDao
import com.birdushenin.newssphere.data.databases.entities.SourceEntity
import com.birdushenin.newssphere.databinding.FragmentSourceBinding
import com.birdushenin.newssphere.domain.NewsService
import com.birdushenin.newssphere.domain.OnSourceItemClickListener
import com.birdushenin.newssphere.navigation.HeadlinesScreens
import com.birdushenin.newssphere.presentation.adapters.SourceAdapter
import com.github.terrakok.cicerone.androidx.FragmentScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import javax.inject.Inject

class SourceFragment : Fragment(), FragmentScreen {

    private lateinit var binding: FragmentSourceBinding

    private var isSearchMode = false
    private val adapter = SourceAdapter()
    private val sourceViewModel: SourceViewModel by activityViewModels()

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var sourceNewsDao: SourceDao

    override fun createFragment(factory: FragmentFactory): Fragment {
        return SourceFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSourceBinding.inflate(layoutInflater)
        MyApplication.appComponent.inject(this)

        binding.progressBar.visibility = View.VISIBLE
        val editText = binding.editSearch

        val newsService = retrofit.create(NewsService::class.java)

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        binding.btnSearch.setOnClickListener {
            if (!isSearchMode) {
                showSearchFragment()
                binding.btnSearch.visibility = View.GONE
                binding.toolbarTitle.visibility = View.GONE
                editText.visibility = View.VISIBLE
            } else {
                hideSearchFragment()
                binding.btnSearch.visibility = View.VISIBLE
                binding.toolbarTitle.visibility = View.VISIBLE
                editText.visibility = View.GONE
            }
        }

        editText.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val query = editText.text.toString()
                lifecycleScope.launch {
                    searchSource(query)
                }
                return@setOnKeyListener true
            }
            false
        }

        binding.refreshPic.setOnClickListener {
            lifecycleScope.launch {
                loadNews(newsService)
            }
            binding.notInternet.text = "Something went wrong Try later"
            Toast.makeText(context, "Not internet connection", Toast.LENGTH_SHORT)
                .show()
        }

        adapter.setOnUserItemClickListener(object : OnSourceItemClickListener {
            override fun onNewsItemClicked(sourceNews: SourceNews) {
                sourceViewModel.selectSource(sourceNews)
                (requireActivity().application as MyApplication).router.navigateTo(HeadlinesScreens.SourceWindowFragment)
            }
        })

        lifecycleScope.launch {
            loadNews(newsService)
        }

        return binding.root
    }

    private fun showSearchFragment() {
        isSearchMode = true
    }

    private fun hideSearchFragment() {
        isSearchMode = false
    }

    private suspend fun loadNews(newsService: NewsService) {
        val apiKey = "4897ed61df034fa4b4bb185141dfe043"
        // eae4e313c2d043c183e78149bc172501 6aae4c71707e4bf4b0bfbe63df5edd15

        try {
            val response = newsService.getSources(apiKey = apiKey)
            binding.refresh.visibility = View.GONE
            binding.refreshPic.visibility = View.GONE
            binding.notInternet.visibility = View.GONE
            binding.notInternetPic.visibility = View.GONE

            if (response.isSuccessful) {
                val newsList = response.body()?.sources ?: emptyList()
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                    val articleResponse = response.body()?.sources ?: emptyList()

                    val sourceEntity = articleResponse.map { article ->
                        SourceEntity(
                            sourceId = article.id,
                            name = article.name,
                            description = article.description,
                            country = article.country,
                            category = article.category,
                            url = article.url,
                            urlToImage = article.urlToImage
                        )
                    }
                    adapter.submitList(newsList)
                    sourceNewsDao.deleteAllArticles()
                    sourceNewsDao.insertArticles(sourceEntity)
                }

            }
        } catch (_: Exception) {
            binding.refresh.visibility = View.VISIBLE
            binding.refreshPic.visibility = View.VISIBLE
            binding.notInternet.visibility = View.VISIBLE
            binding.notInternetPic.visibility = View.VISIBLE
        }
    }

    private suspend fun searchSource(query: String) {
        val searchResults: List<SourceNews>
        val offlineArticles = sourceNewsDao.searchArticles(query)
        searchResults = offlineArticles.map { articleEntity ->
            SourceNews(
                name = articleEntity.name,
                description = articleEntity.description,
                country = articleEntity.country,
                category = articleEntity.category,
                urlToImage = articleEntity.urlToImage,
                url = articleEntity.url,
                id = articleEntity.sourceId
            )
        }
        withContext(Dispatchers.Main) {
            adapter.submitList(searchResults)
        }
    }
}