package com.birdushenin.newssphere.domain

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.birdushenin.newssphere.data.databases.SavedDatabase
import java.util.Calendar

class ClearOldDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return try {
            val thresholdDate = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, -14)
            }.timeInMillis

            val savedArticleDao = SavedDatabase.getDatabase(applicationContext).newsDao()
            savedArticleDao.deleteOldSavedNews(thresholdDate)

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}