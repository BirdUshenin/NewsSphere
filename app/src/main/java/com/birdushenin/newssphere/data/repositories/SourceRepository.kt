package com.birdushenin.newssphere.data.repositories

import com.birdushenin.newssphere.data.SourceNews
import com.birdushenin.newssphere.data.databases.daos.SourceDao
import com.birdushenin.newssphere.data.databases.entities.SourceEntity
import com.birdushenin.newssphere.domain.NewsService
import javax.inject.Inject

class SourceRepository @Inject constructor(
    private val sourceDao: SourceDao,
    private val newsService: NewsService
) {

    suspend fun loadNews(): List<SourceNews> {
        val apiKey = "4897ed61df034fa4b4bb185141dfe043"

        try {
            val response = newsService.getSources(apiKey = apiKey)

            if (response.isSuccessful) {
                val newsList = response.body()?.sources ?: emptyList()

                val sourceEntity = newsList.map { article ->
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

                sourceDao.deleteAllArticles()
                sourceDao.insertArticles(sourceEntity)

                return newsList
            }
        } catch (e: Exception) { }
        return emptyList()
    }
}