package com.birdushenin.newssphere.domain

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.birdushenin.newssphere.MyApplication
import com.birdushenin.newssphere.data.databases.daos.SavedNewsDao
import java.util.Calendar
import javax.inject.Inject

class ClearOldDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    init {
        MyApplication.appComponent.inject(this)
    }

    @Inject
    lateinit var newsDao: SavedNewsDao

    override suspend fun doWork(): Result {
        return try {
            val thresholdDate = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, -14)
            }.timeInMillis

            newsDao.deleteOldSavedNews(thresholdDate)

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}