package com.birdushenin.newssphere.data.repositories

import androidx.lifecycle.LiveData
import com.birdushenin.newssphere.data.databases.daos.ArticleDao
import com.birdushenin.newssphere.data.databases.daos.SavedNewsDao
import com.birdushenin.newssphere.data.databases.entities.ArticleEntity
import com.birdushenin.newssphere.data.databases.entities.SavedNewsEntity
import javax.inject.Inject

class DatabaseRepository @Inject constructor(
    private val articleDao: ArticleDao,
    private val savedNewsDao: SavedNewsDao,
) {
    suspend fun insertArticles(articles: List<ArticleEntity>) {
        articleDao.insertArticles(articles)
    }

    suspend fun deleteAllArticles() {
        articleDao.deleteAllArticles()
    }

    suspend fun getAllArticles(): List<ArticleEntity> {
        return articleDao.getAllArticles()
    }

    suspend fun insertSavedNews(savedNews: SavedNewsEntity) {
        return savedNewsDao.insertSavedNews(savedNews)
    }

    suspend fun deleteOldSavedNews(thresholdDate: Long) {
        return savedNewsDao.deleteOldSavedNews(thresholdDate)
    }

    fun getAllSavedNews(): LiveData<List<SavedNewsEntity>> {
        return savedNewsDao.getAllSavedNews()
    }
}
