package com.birdushenin.newssphere.data.databases.daos

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import com.birdushenin.newssphere.data.databases.entities.SourceEntity

@Dao
interface SourceDao {
    @Query("SELECT * FROM sources")
    suspend fun getAllArticles(): List<SourceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<SourceEntity>)

    @Query("SELECT * FROM sources WHERE name LIKE '%' || :query || '%' OR id LIKE '%' || :query || '%'")
    suspend fun searchArticles(query: String): List<SourceEntity>

    @Query("DELETE FROM sources")
    suspend fun deleteAllArticles()
}
