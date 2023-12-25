package com.birdushenin.newssphere.domain

import com.birdushenin.newssphere.data.databases.daos.SavedNewsDao

class SavedRepository(private val savedDao: SavedNewsDao) {

//    suspend fun delete(savedClass: SavedClass) {
//        savedDao.deleteByUrl(savedClass.urlText ?: "")
//    }
//
//    fun isArticleSaved(savedClass: SavedClass): Boolean {
//        return savedDao.isArticleSaved(savedClass.urlText)
//    }
}