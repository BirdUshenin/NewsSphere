package com.birdushenin.newssphere.domain.usecases

import com.birdushenin.newssphere.data.SourceNews
import com.birdushenin.newssphere.data.repositories.SourceRepository
import javax.inject.Inject

class SourceUseCase @Inject constructor(
    private val sourceRepository: SourceRepository
) {
    suspend fun loadNews(): List<SourceNews> {
        return sourceRepository.loadNews()
    }
}