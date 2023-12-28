package com.birdushenin.newssphere.domain.usecases

import com.birdushenin.newssphere.data.Article
import com.birdushenin.newssphere.data.Source
import com.birdushenin.newssphere.data.databases.entities.ArticleEntity
import com.birdushenin.newssphere.data.repositories.BusinessRepositories
import com.birdushenin.newssphere.data.repositories.DatabaseRepository
import javax.inject.Inject

class BusinessUseCase @Inject constructor(
    private val networkRepository: BusinessRepositories,
    private val databaseRepository: DatabaseRepository
) {
    suspend fun loadNews(
        filter: String,
        fromDate: String?,
        toDate: String?,
        language: String?
    ): List<Article> {
        return try {
            val articles = networkRepository.loadNews(filter, fromDate, toDate, language)

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
            databaseRepository.deleteAllArticles()
            databaseRepository.insertArticles(articleEntities)

            articles

        } catch (e: Exception) {
            val offlineArticlesList = getOfflineData()
            offlineArticlesList
        }
    }

    private suspend fun getOfflineData(): List<Article> {
        val offlineArticles = databaseRepository.getAllArticles()

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
