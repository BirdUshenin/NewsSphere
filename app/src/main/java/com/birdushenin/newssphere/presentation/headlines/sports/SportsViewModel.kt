package com.birdushenin.newssphere.presentation.headlines.sports

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.birdushenin.newssphere.data.Article
import com.birdushenin.newssphere.data.Source
import com.birdushenin.newssphere.data.databases.daos.ArticleDao
import com.birdushenin.newssphere.data.databases.entities.ArticleEntity
import com.birdushenin.newssphere.domain.usecases.SportsUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class SportsViewModel @Inject constructor(
    private val articleDao: ArticleDao,
    private val sportsUseCase: SportsUseCase
) : ViewModel() {
    val news: LiveData<List<Article>>
        get() = _news
    private val _news = MutableLiveData<List<Article>>()

    init {
        viewModelScope.launch {
            _news.value = loadNews("", null, null, null)
        }
    }

    fun updateNews(
        selectedFilter: String,
        selectedCalendarStart: String?,
        selectedCalendarEnd: String?,
        selectedLang: String?
    ) {
        viewModelScope.launch {
            _news.value = loadNews(
                selectedFilter,
                selectedCalendarStart,
                selectedCalendarEnd,
                selectedLang
            )
        }
    }

    private suspend fun loadNews(
        filter: String,
        fromDate: String?,
        toDate: String?,
        language: String?
    ): List<Article> {

        return try {
            val articles =
                sportsUseCase.loadNews(filter, fromDate, toDate, language)

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

            articleDao.deleteAllArticles()

            articleDao.insertArticles(articleEntities)
            articles
        } catch (_: Exception) {
            val offlineArticlesList = getOfflineData()
            offlineArticlesList
        }
    }

    private suspend fun getOfflineData(): List<Article> {
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
        return offlineArticlesList
    }

}

class SportsViewModelFactory @Inject constructor(
    myViewModelProvider: Provider<SportsViewModel>
) : ViewModelProvider.Factory {

    private val providers = mapOf<Class<*>, Provider<out ViewModel>>(
        SportsViewModel::class.java to myViewModelProvider
    )

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return providers[modelClass]!!.get() as T
    }
}

