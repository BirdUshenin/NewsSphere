package com.birdushenin.newssphere.data.repositories

import com.birdushenin.newssphere.data.Article
import com.birdushenin.newssphere.domain.NewsService
import javax.inject.Inject

class NetworkRepository @Inject constructor(
    private val newsService: NewsService,
) {
    suspend fun loadNews(
        filter: String,
        fromDate: String?,
        toDate: String?,
        language: String?
    ): List<Article> {
        val query = "general"
        val apiKey = "eae4e313c2d043c183e78149bc172501"
        // 6aae4c71707e4bf4b0bfbe63df5edd15 eae4e313c2d043c183e78149bc172501

        val response = newsService.getEverything(query, apiKey, fromDate, toDate, filter, language)
        return if (response.isSuccessful) {
            return response.body()?.articles ?: emptyList()
        } else {
            listOf()
        }
    }

}
