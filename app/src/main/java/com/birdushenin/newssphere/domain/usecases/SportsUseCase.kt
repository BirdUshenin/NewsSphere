package com.birdushenin.newssphere.domain.usecases

import com.birdushenin.newssphere.data.Article
import com.birdushenin.newssphere.data.repositories.SportsRepositories
import javax.inject.Inject

class SportsUseCase @Inject constructor(
    private val networkRepository: SportsRepositories
) {
    suspend fun loadNews(
        filter: String,
        fromDate: String?,
        toDate: String?,
        language: String?
    ): List<Article> {
        return networkRepository.loadNews(
            filter,
            fromDate,
            toDate,
            language,
        )
    }
}