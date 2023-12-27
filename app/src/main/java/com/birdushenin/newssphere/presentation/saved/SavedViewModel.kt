package com.birdushenin.newssphere.presentation.saved

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.birdushenin.newssphere.data.SavedClass
import com.birdushenin.newssphere.data.databases.NewsDatabase
import com.birdushenin.newssphere.data.databases.entities.SavedNewsEntity
import com.birdushenin.newssphere.domain.ClearOldDataWorker
import kotlinx.coroutines.launch

class SavedViewModel(application: Application) : AndroidViewModel(application) {
    // TODO inject dao in viewModel right way
    val savedArticleDao = NewsDatabase.getDatabase(application).savedNewsDao()
    val selectedArticle: LiveData<List<SavedNewsEntity>> = savedArticleDao.getAllSavedNews()

    private val workManager = WorkManager.getInstance(application)

    fun selectArticle(savedClass: SavedClass) {
        viewModelScope.launch {
            val title = savedClass.titleText
            val url = savedClass.urlText
            val currentTimeMillis = System.currentTimeMillis()

            if (savedArticleDao.getSavedNewsByTitleAndUrl(title, url) == null) {
                val savedArticleEntity = SavedNewsEntity(
                    titleText = title,
                    urlText = url,
                    descriptionText = savedClass.descriptionText,
                    sourceText = savedClass.sourceText,
                    imagePic = savedClass.imagePic,
                    publishedAt = savedClass.publishedAt,
                    timestamp = currentTimeMillis,
                    content = savedClass.content
                )
                savedArticleDao.insertSavedNews(savedArticleEntity)
                scheduleClearOldDataWork()
            }
        }
    }

    private fun scheduleClearOldDataWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()

        val clearOldDataRequest = OneTimeWorkRequestBuilder<ClearOldDataWorker>()
            .setConstraints(constraints)
            .build()

        workManager.enqueue(clearOldDataRequest)
    }

    fun deleteArticle(savedClass: SavedClass) {
        viewModelScope.launch {
            savedArticleDao.deleteSavedNews(savedClass.titleText, savedClass.urlText)
        }
    }
}