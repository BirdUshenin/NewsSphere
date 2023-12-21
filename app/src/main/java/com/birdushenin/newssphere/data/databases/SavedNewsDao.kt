package com.birdushenin.newssphere.data.databases

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SavedNewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedNews(savedNews: SavedNewsEntity)

    @Query("SELECT * FROM saved_news")
    fun getAllSavedNews(): LiveData<List<SavedNewsEntity>>
}