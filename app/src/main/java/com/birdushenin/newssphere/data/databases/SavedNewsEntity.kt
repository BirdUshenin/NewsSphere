package com.birdushenin.newssphere.data.databases

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_news")
data class SavedNewsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titleText: String?,
    val urlText: String?,
    val descriptionText: String?,
    val sourceText: String?,
    val imagePic: String?,


    @ColumnInfo(name = "timestamp")
    val timestamp: Long
)