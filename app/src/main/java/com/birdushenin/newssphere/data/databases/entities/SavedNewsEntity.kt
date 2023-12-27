package com.birdushenin.newssphere.data.databases.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "saved_news",
    indices = [Index(value = ["titleText", "urlText"], unique = true)],
)
data class SavedNewsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titleText: String?,
    val urlText: String?,
    val descriptionText: String?,
    val sourceText: String?,
    val publishedAt: String?,
    val imagePic: String?,
    val content: String?,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long
)