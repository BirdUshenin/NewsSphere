package com.birdushenin.newssphere.presentation

import android.os.Bundle
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
import com.birdushenin.newssphere.data.databases.ArticleEntity
import com.birdushenin.newssphere.data.databases.NewsDatabase
import com.birdushenin.newssphere.databinding.FragmentGeneralBinding
import com.birdushenin.newssphere.domain.NewsService
import com.birdushenin.newssphere.domain.OnNewsItemClickListener
import com.birdushenin.newssphere.navigation.Screens
import com.birdushenin.newssphere.presentation.adapter.NewsAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import javax.inject.Inject

class GeneralFragment : Fragment() {

    private val adapter = NewsAdapter()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val sharedViewModel: NewsViewModel by activityViewModels()
    private val searchViewModel: SearchViewModel by activityViewModels()
    private val filterViewModel: FilterViewModel by activityViewModels()

    @Inject
    lateinit var retrofit: Retrofit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentGeneralBinding.inflate(layoutInflater)
        MyApplication.appComponent.inject(this)

        val newsService = retrofit.create(NewsService::class.java)

        val selectedFilter = filterViewModel.selectedFilter.value?.selectedPopular
        val selectedCalendarStart = filterViewModel.selectedFilter.value?.selectedCalendarStart
        val selectedCalendarEnd = filterViewModel.selectedFilter.value?.selectedCalendarEnd

        swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                if (selectedFilter == null) {
                    loadNews(newsService, "popular", null, null)
                } else {
                    loadNews(newsService, selectedFilter, selectedCalendarStart, selectedCalendarEnd)
                }
                swipeRefreshLayout.isRefreshing = false
            }
        }

        filterViewModel.selectedFilter.observe(viewLifecycleOwner, Observer { (selectedFilter, selectedCalendarStart, selectedCalendarEnd) ->
            lifecycleScope.launch {
                if (selectedFilter != null) {
                    loadNews(newsService, selectedFilter, selectedCalendarStart, selectedCalendarEnd)
                }
            }
        })

        searchViewModel.searchQuery.observe(viewLifecycleOwner, Observer { query ->
            lifecycleScope.launch {
                adapter.submitList(emptyList())
                if (query?.isNotBlank() == true) {
                    searchArticles(query)
                } else {
                    loadNews(newsService, "popular", null, null)
                }
            }
        })

        //RecyclerView
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        adapter.setOnUserItemClickListener(object : OnNewsItemClickListener {
            override fun onNewsItemClicked(article: Article) {
                sharedViewModel.selectArticle(article)
                (requireActivity().application as MyApplication).router.navigateTo(Screens.NewsWindowScreen)
            }
        })

        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            binding.progressBar.visibility = View.GONE
            if (selectedFilter == null) {
                loadNews(newsService, "popular", null, null)
            } else {
                loadNews(newsService, selectedFilter, selectedCalendarStart, selectedCalendarEnd)
            }
        }
        return binding.root
    }

    private suspend fun searchArticles(query: String) {
        val articleDao = NewsDatabase.getDatabase(requireActivity().applicationContext).articleDao()
        val searchResults = articleDao.searchArticles(query)

        val searchResultsList = searchResults.map { articleEntity ->
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

        withContext(Dispatchers.Main) {
            adapter.submitList(searchResultsList)
        }
    }

    private suspend fun loadNews(newsService: NewsService, filter: String, fromDate: String?, toDate: String? ) {
        val query = "general"
        val apiKey = "d777c1dfe5e746a0b6363c268f0f61a8"

        try {
            val response = newsService.getEverything(query, apiKey, fromDate, toDate, filter)
            if (response.isSuccessful) {
                val articles = response.body()?.articles ?: emptyList()

                val articleEntities = articles.map { article ->
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
                withContext(Dispatchers.Main) {
                    adapter.submitList(articles)
                    val articleDao =
                        NewsDatabase.getDatabase(requireActivity().applicationContext).articleDao()
                    articleDao.insertArticles(articleEntities)
                    //TODO in other tread
                }
            }
        } catch (_: Exception) {
            val articleDao = NewsDatabase.getDatabase(requireContext()).articleDao()
            val offlineArticles = articleDao.getAllArticles()

            val offlineArticlesList = offlineArticles.map { articleEntity ->
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
            withContext(Dispatchers.Main) {
                adapter.submitList(offlineArticlesList)
            }
        }
    }
}