package com.birdushenin.newssphere.data.databases

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArticleDao {

    @Query("SELECT * FROM articles")
    suspend fun getAllArticles(): List<ArticleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ArticleEntity>)

    @Query("SELECT * FROM articles WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    suspend fun searchArticles(query: String): List<ArticleEntity>
}