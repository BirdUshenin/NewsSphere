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
import com.birdushenin.newssphere.databinding.FragmentSportsBinding
import com.birdushenin.newssphere.domain.NewsService
import com.birdushenin.newssphere.domain.OnNewsItemClickListener
import com.birdushenin.newssphere.navigation.HeadlinesScreens
import com.birdushenin.newssphere.presentation.headlines.SearchViewModel
import com.birdushenin.newssphere.presentation.headlines.UpdateViewModel
import com.birdushenin.newssphere.presentation.headlines.filters.FilterViewModel
import com.birdushenin.newssphere.presentation.headlines.sports.SportsFragment
import com.birdushenin.newssphere.presentation.headlines.sports.SportsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference

class SportsPresenter(
    private val sportsViewModel: SportsViewModel,
    private val filterViewModel: FilterViewModel,
    private val searchViewModel: SearchViewModel,
    private val updateViewModel: UpdateViewModel
) {

    private lateinit var fragment: SportsFragment
    private lateinit var binding: FragmentSportsBinding
    private var fragmentPreference: WeakReference<SportsFragment>? = null

    fun attach(fragment: SportsFragment, binding: FragmentSportsBinding) {
        fragmentPreference = WeakReference(fragment)
        this.fragment = fragment
        this.binding = binding
        initializeUI()
        observeViewModels()
    }

    private fun initializeUI() {
        val fragment = fragmentPreference?.get()
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
                    sportsViewModel.updateNews("popular", null, null, null)
                } else {
                    sportsViewModel.updateNews(
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
                    sportsViewModel.updateNews(
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
                    sportsViewModel.updateNews("popular", null, null, null)
                }
            }
        })

        updateViewModel.buttonClicked.observe(fragment.viewLifecycleOwner) {
            fragment.lifecycleScope.launch {
                sportsViewModel.updateNews("popular", null, null, null)
            }
        }

        sportsViewModel.news.observe(fragment.viewLifecycleOwner) {
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