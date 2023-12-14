package com.birdushenin.newssphere.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import javax.inject.Inject

class GeneralFragment : Fragment() {

    private val adapter = NewsAdapter()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val sharedViewModel: SharedViewModel by activityViewModels()

    @Inject
    lateinit var retrofit: Retrofit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentGeneralBinding.inflate(layoutInflater)
        MyApplication.appComponent.inject(this)

        val newsService = retrofit.create(NewsService::class.java)

        swipeRefreshLayout = binding.swipeRefreshLayout

        // Set up refresh listener
        swipeRefreshLayout.setOnRefreshListener {
            // Refresh data when the user performs a swipe-to-refresh gesture
            lifecycleScope.launch {
                loadNews(newsService)
                // Signal that refresh is complete
                swipeRefreshLayout.isRefreshing = false
            }
        }

        //RecyclerView
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        adapter.setOnUserItemClickListener(object: OnNewsItemClickListener {
            override fun onNewsItemClicked(article: Article) {
                sharedViewModel.selectArticle(article)
                (requireActivity().application as MyApplication).router.navigateTo(Screens.TestScreen)
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
                    val articleDao = NewsDatabase.getDatabase(requireActivity().applicationContext).articleDao()
                    articleDao.insertArticles(articleEntities)
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