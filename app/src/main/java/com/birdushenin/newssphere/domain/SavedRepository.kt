package com.birdushenin.newssphere.domain

import androidx.lifecycle.LiveData
import com.birdushenin.newssphere.data.SavedClass
import com.birdushenin.newssphere.data.databases.SavedNewsDao
import com.birdushenin.newssphere.data.databases.SavedNewsEntity

class SavedRepository(private val savedDao: SavedNewsDao) {

//    suspend fun delete(savedClass: SavedClass) {
//        savedDao.deleteByUrl(savedClass.urlText ?: "")
//    }
//
//    fun isArticleSaved(savedClass: SavedClass): Boolean {
//        return savedDao.isArticleSaved(savedClass.urlText)
//    }
}