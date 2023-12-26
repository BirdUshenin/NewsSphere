package com.birdushenin.newssphere.data.databases.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sources")
data class SourceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sourceId: String?,
    val name: String?,
    val description: String?,
    val country: String?,
    val category: String?,
    val url: String?,
    val urlToImage: String?
)