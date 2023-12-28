package com.birdushenin.newssphere.mvp

import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.birdushenin.newssphere.MyApplication
import com.birdushenin.newssphere.data.Article
import com.birdushenin.newssphere.data.Source
import com.birdushenin.newssphere.data.databases.entities.ArticleEntity
import com.birdushenin.newssphere.databinding.FragmentBusinessBinding
import com.birdushenin.newssphere.domain.NewsService
import com.birdushenin.newssphere.domain.OnNewsItemClickListener
import com.birdushenin.newssphere.navigation.HeadlinesScreens
import com.birdushenin.newssphere.presentation.headlines.SearchViewModel
import com.birdushenin.newssphere.presentation.headlines.UpdateViewModel
import com.birdushenin.newssphere.presentation.headlines.business.BusinessFragment
import com.birdushenin.newssphere.presentation.headlines.business.BusinessViewModel
import com.birdushenin.newssphere.presentation.headlines.filters.FilterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference

class BusinessPresenter(
    private val businessViewModel: BusinessViewModel,
    private val filterViewModel: FilterViewModel,
    private val searchViewModel: SearchViewModel,
    private val updateViewModel: UpdateViewModel
) {
    private var fragmentReference: WeakReference<BusinessFragment>? = null
    private lateinit var fragment: BusinessFragment
    private lateinit var binding: FragmentBusinessBinding

    fun attach(fragment: BusinessFragment, binding: FragmentBusinessBinding) {
        this.fragment = fragment
        fragmentReference = WeakReference(fragment)
        this.binding = binding
        initializeUI()
        observeViewModels()
    }

    private fun initializeUI() {
        val fragment = fragmentReference?.get()
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(fragment?.context)
        recyclerView.adapter = fragment?.adapter

        fragment?.adapter?.setOnUserItemClickListener(object : OnNewsItemClickListener {
            override fun onNewsItemClicked(article: Article) {
                fragment.sharedViewModel.selectArticle(article)
                (fragment.requireActivity().application as MyApplication).router.navigateTo(
                    HeadlinesScreens.NewsWindowScreen
                )
            }
        })

        fragment?.swipeRefreshLayout = binding.swipeRefreshLayout
        fragment?.swipeRefreshLayout?.setOnRefreshListener {
            fragment.lifecycleScope.launch {
                val selectedFilter = filterViewModel.selectedFilter.value?.selectedPopular
                val selectedCalendarStart =
                    filterViewModel.selectedFilter.value?.selectedCalendarStart
                val selectedCalendarEnd = filterViewModel.selectedFilter.value?.selectedCalendarEnd
                val selectedLang = filterViewModel.selectedFilter.value?.selectedLang

                if (selectedFilter == null) {
                    businessViewModel.updateNews("popular", null, null, null)
                } else {
                    businessViewModel.updateNews(
                        selectedFilter,
                        selectedCalendarStart,
                        selectedCalendarEnd,
                        selectedLang
                    )
                }
                fragment.swipeRefreshLayout.isRefreshing = false
            }
        }

        binding.progressBar.visibility = View.VISIBLE
    }

    private fun observeViewModels() {
        filterViewModel.selectedFilter.observe(fragment.viewLifecycleOwner) { (selectedFilter, selectedCalendarStart, selectedCalendarEnd, selectedLang) ->
            fragment.lifecycleScope.launch {
                if (selectedFilter != null) {
                    businessViewModel.updateNews(
                        selectedFilter,
                        selectedCalendarStart,
                        selectedCalendarEnd,
                        selectedLang
                    )
                }
            }
        }

        searchViewModel.searchQuery.observe(fragment.viewLifecycleOwner, Observer { query ->
            fragment.lifecycleScope.launch {
                fragment.adapter.submitList(emptyList())
                if (query?.isNotBlank() == true) {
                    searchArticles(query)
                } else {
                    businessViewModel.updateNews("popular", null, null, null)
                }
            }
        })

        updateViewModel.buttonClicked.observe(fragment.viewLifecycleOwner) {
            fragment.lifecycleScope.launch {
                businessViewModel.updateNews("popular", null, null, null)
            }
        }

        businessViewModel.news.observe(fragment.viewLifecycleOwner) {
            fragment.adapter.submitList(it)
            binding.progressBar.visibility = View.GONE
        }

    }

    private suspend fun searchArticles(query: String) {
        val context = fragment.requireActivity().applicationContext
        val apiKey = "4897ed61df034fa4b4bb185141dfe043"
        var searchResults: List<Article>

        if (isInternetAvailable(context)) {
            try {
                val newsService = fragment.retrofit.create(NewsService::class.java)
                val response = newsService.getEverything(query, apiKey, null, null, null, null)

                if (response.isSuccessful) {
                    searchResults = response.body()?.articles ?: emptyList()
                    val articleEntities = searchResults.map { article ->
                        ArticleEntity(
                            sourceId = article.source.id,
                            sourceName = article.source.name,
                            author = article.author,
                            title = article.title,
                            description = article.description,
                            url = article.url,
                            urlToImage = article.urlToImage,
                            publishedAt = article.publishedAt,
                            content = article.content
                        )
                    }
                    fragment.articleDao.deleteAllArticles()
                    fragment.articleDao.insertArticles(articleEntities)
                } else {
                    searchResults = emptyList()
                }
            } catch (e: Exception) {
                searchResults = emptyList()
            }
        } else {
            val offlineArticles = fragment.articleDao.searchArticles(query)
            searchResults = offlineArticles.map { articleEntity ->
                Article(
                    source = Source(articleEntity.sourceId, articleEntity.sourceName),
                    author = articleEntity.author,
                    title = articleEntity.title,
                    description = articleEntity.description,
                    url = articleEntity.url,
                    urlToImage = articleEntity.urlToImage,
                    publishedAt = articleEntity.publishedAt,
                    content = articleEntity.content
                )
            }
        }

        withContext(Dispatchers.Main) {
            binding.progressBar.visibility = View.GONE
            fragment.adapter.submitList(searchResults)
        }
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}
