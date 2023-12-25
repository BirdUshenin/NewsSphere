package com.birdushenin.newssphere.presentation.headlines.general

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.birdushenin.newssphere.MyApplication
import com.birdushenin.newssphere.data.Article
import com.birdushenin.newssphere.data.Source
import com.birdushenin.newssphere.data.databases.daos.ArticleDao
import com.birdushenin.newssphere.data.databases.entities.ArticleEntity
import com.birdushenin.newssphere.databinding.FragmentGeneralBinding
import com.birdushenin.newssphere.domain.NewsService
import com.birdushenin.newssphere.domain.OnNewsItemClickListener
import com.birdushenin.newssphere.navigation.Screens
import com.birdushenin.newssphere.presentation.headlines.NewsViewModel
import com.birdushenin.newssphere.presentation.headlines.SearchViewModel
import com.birdushenin.newssphere.presentation.headlines.UpdateViewModel
import com.birdushenin.newssphere.presentation.adapters.NewsAdapter
import com.birdushenin.newssphere.presentation.headlines.filters.FilterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import javax.inject.Inject

class GeneralFragment : Fragment() {

    private lateinit var binding: FragmentGeneralBinding

    private val adapter = NewsAdapter()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val generalViewModel: GeneralViewModel by activityViewModels {
        MyApplication.appComponent.viewModelsFactory()
    }

    private val updateViewModel: UpdateViewModel by activityViewModels()
    private val sharedViewModel: NewsViewModel by activityViewModels()
    private val searchViewModel: SearchViewModel by activityViewModels()
    private val filterViewModel: FilterViewModel by activityViewModels()

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var articleDao: ArticleDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGeneralBinding.inflate(layoutInflater)
        MyApplication.appComponent.inject(this)

        val selectedFilter = filterViewModel.selectedFilter.value?.selectedPopular
        val selectedCalendarStart = filterViewModel.selectedFilter.value?.selectedCalendarStart
        val selectedCalendarEnd = filterViewModel.selectedFilter.value?.selectedCalendarEnd
        val selectedLang = filterViewModel.selectedFilter.value?.selectedLang

        swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                if (selectedFilter == null) {
                    generalViewModel.updateNews("popular", null, null, null)
                } else {
                    generalViewModel.updateNews(
                        selectedFilter,
                        selectedCalendarStart,
                        selectedCalendarEnd,
                        selectedLang
                    )
                }
                swipeRefreshLayout.isRefreshing = false
            }
        }

        filterViewModel.selectedFilter.observe(viewLifecycleOwner) { (selectedFilter, selectedCalendarStart, selectedCalendarEnd, selectedLang) ->
            lifecycleScope.launch {
                if (selectedFilter != null) {
                    generalViewModel.updateNews(
                        selectedFilter,
                        selectedCalendarStart,
                        selectedCalendarEnd,
                        selectedLang
                    )
                }
            }
        }

        searchViewModel.searchQuery.observe(viewLifecycleOwner, Observer { query ->
            lifecycleScope.launch {

                adapter.submitList(emptyList())
                if (query?.isNotBlank() == true) {
                    searchArticles(query)
                } else {
                    generalViewModel.updateNews("popular", null, null, null)
                }
            }
        })

        updateViewModel.buttonClicked.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                generalViewModel.updateNews("popular", null, null, null)
            }
        }

        //RecyclerView
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        // TODO Предусмотреть кнопку для быстрого перехода вверх
        //        recyclerView.smoothScrollToPosition(0)
        adapter.setOnUserItemClickListener(object : OnNewsItemClickListener {
            override fun onNewsItemClicked(article: Article) {
                sharedViewModel.selectArticle(article)
                (requireActivity().application as MyApplication).router.navigateTo(Screens.NewsWindowScreen)
            }
        })

        binding.progressBar.visibility = View.VISIBLE

        generalViewModel.news.observe(viewLifecycleOwner) {
            Log.d("okmmmm", "generalViewModel.news: ${it.size}")
            adapter.submitList(it)
            binding.progressBar.visibility = View.GONE
        }

        return binding.root
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private suspend fun searchArticles(query: String) {
        val context = requireActivity().applicationContext
        val apiKey = "6aae4c71707e4bf4b0bfbe63df5edd15"
        var searchResults: List<Article>

        if (isInternetAvailable(context)) {
            try {
                val newsService = retrofit.create(NewsService::class.java)
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
                    articleDao.deleteAllArticles()
                    articleDao.insertArticles(articleEntities)
                } else {
                    searchResults = emptyList()
                }
            } catch (e: Exception) {
                searchResults = emptyList()
            }
        } else {
            val offlineArticles = articleDao.searchArticles(query)
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
            adapter.submitList(searchResults)
        }
    }
}