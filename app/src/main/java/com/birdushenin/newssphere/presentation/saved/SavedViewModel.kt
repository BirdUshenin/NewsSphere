package com.birdushenin.newssphere.presentation.saved

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.widget.Toast
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
    private val savedArticleDao = NewsDatabase.getDatabase(application).savedNewsDao()
    val selectedArticle: LiveData<List<SavedNewsEntity>> = savedArticleDao.getAllSavedNews()

    @SuppressLint("StaticFieldLeak")
    private val context: Context = application.applicationContext

    private val workManager = WorkManager.getInstance(application)

    var isArticleSaved = false

    fun selectArticle(savedClass: SavedClass) {
        val title = savedClass.titleText
        val url = savedClass.urlText
        val currentTimeMillis = System.currentTimeMillis()

        viewModelScope.launch {

            val savedArticleEntity = SavedNewsEntity(
                titleText = title,
                urlText = url,
                descriptionText = savedClass.descriptionText,
                sourceText = savedClass.sourceText,
                imagePic = savedClass.imagePic,
                timestamp = currentTimeMillis
            )
            Toast.makeText(context, "News saved", Toast.LENGTH_SHORT).show()
            savedArticleDao.insertSavedNews(savedArticleEntity)
            scheduleClearOldDataWork()
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
            Toast.makeText(context, "You delete this news", Toast.LENGTH_SHORT).show()
        }
    }
}