package com.birdushenin.newssphere.data.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.birdushenin.newssphere.data.databases.daos.ArticleDao
import com.birdushenin.newssphere.data.databases.daos.SavedNewsDao
import com.birdushenin.newssphere.data.databases.daos.SourceDao
import com.birdushenin.newssphere.data.databases.entities.ArticleEntity
import com.birdushenin.newssphere.data.databases.entities.SavedNewsEntity
import com.birdushenin.newssphere.data.databases.entities.SourceEntity

@Database(
    entities = [
        ArticleEntity::class,
        SavedNewsEntity::class,
        SourceEntity::class
    ], version = 1, exportSchema = false
)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao
    abstract fun savedNewsDao(): SavedNewsDao
    abstract fun sourceNewsDao(): SourceDao

    companion object {

        private const val DATABASE_NAME = "news_database"

        @Volatile
        private var INSTANCE: NewsDatabase? = null

        fun getDatabase(context: Context): NewsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NewsDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}