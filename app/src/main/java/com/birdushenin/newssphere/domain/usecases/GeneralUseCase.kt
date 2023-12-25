package com.birdushenin.newssphere.domain.usecases

import com.birdushenin.newssphere.data.Article
import com.birdushenin.newssphere.data.repositories.DatabaseRepository
import com.birdushenin.newssphere.data.repositories.NetworkRepository
import javax.inject.Inject

class GeneralUseCase @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val databaseRepository: DatabaseRepository,
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