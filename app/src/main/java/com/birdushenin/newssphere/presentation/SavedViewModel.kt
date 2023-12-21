package com.birdushenin.newssphere.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.birdushenin.newssphere.data.SavedClass
import com.birdushenin.newssphere.data.databases.AppDatabase
import com.birdushenin.newssphere.data.databases.SavedNewsEntity
import kotlinx.coroutines.launch

class SavedViewModel(application: Application) : AndroidViewModel(application) {
    private val savedArticleDao = AppDatabase.getDatabase(application).newsDao()
    val selectedArticle: LiveData<List<SavedNewsEntity>> = savedArticleDao.getAllSavedNews()

    fun selectArticle(savedClass: SavedClass) {
        val savedArticleEntity = SavedNewsEntity(
            titleText = savedClass.titleText,
            urlText = savedClass.urlText,
            descriptionText = savedClass.descriptionText,
            sourceText = savedClass.sourceText,
            imagePic = savedClass.imagePic
        )
        viewModelScope.launch {
            savedArticleDao.insertSavedNews(savedArticleEntity)
        }
    }
}