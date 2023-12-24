package com.birdushenin.newssphere.data.databases

import androidx.lifecycle.LiveData
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SavedNewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedNews(savedNews: SavedNewsEntity)

    @Query("SELECT * FROM saved_news WHERE titleText = :title AND urlText = :url LIMIT 1")
    suspend fun getSavedNewsByTitleAndUrl(title: String?, url: String?): SavedNewsEntity?

    @Query("DELETE FROM saved_news WHERE timestamp < :thresholdDate")
    suspend fun deleteOldSavedNews(thresholdDate: Long)

    @Query("SELECT * FROM saved_news")
    fun getAllSavedNews(): LiveData<List<SavedNewsEntity>>

    @Query("DELETE FROM saved_news WHERE titleText = :title AND urlText = :url")
    suspend fun deleteSavedNews(title: String?, url: String?)

}