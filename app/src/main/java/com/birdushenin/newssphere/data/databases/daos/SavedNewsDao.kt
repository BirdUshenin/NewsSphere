package com.birdushenin.newssphere.data.databases.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.birdushenin.newssphere.data.databases.entities.ArticleEntity
import com.birdushenin.newssphere.data.databases.entities.SavedNewsEntity

@Dao
interface SavedNewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedNews(savedNews: SavedNewsEntity)

    // TODO use this function to check if a news already exists
    @Query("SELECT * FROM saved_news WHERE titleText = :title AND urlText = :url LIMIT 1")
    suspend fun getSavedNewsByTitleAndUrl(title: String?, url: String?): SavedNewsEntity?

    @Query("SELECT * FROM saved_news WHERE titleText LIKE '%' || :query || '%' OR descriptionText LIKE '%' || :query || '%'")
    suspend fun searchArticles(query: String): List<SavedNewsEntity>

    @Query("DELETE FROM saved_news WHERE timestamp < :thresholdDate")
    suspend fun deleteOldSavedNews(thresholdDate: Long)

    @Query("SELECT * FROM saved_news")
    fun getAllSavedNews(): LiveData<List<SavedNewsEntity>>

    @Query("DELETE FROM saved_news WHERE titleText = :title AND urlText = :url")
    suspend fun deleteSavedNews(title: String?, url: String?)

}