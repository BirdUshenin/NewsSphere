package com.birdushenin.newssphere.di.module

import android.content.Context
import com.birdushenin.newssphere.data.databases.NewsDatabase
import com.birdushenin.newssphere.data.databases.daos.ArticleDao
import com.birdushenin.newssphere.data.databases.daos.SavedNewsDao
import com.birdushenin.newssphere.data.databases.daos.SourceDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideNewsDatabase(context: Context): NewsDatabase {
        return NewsDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun provideArticlesDao(newsDatabase: NewsDatabase): ArticleDao {
        return newsDatabase.articleDao()
    }

    @Singleton
    @Provides
    fun provideNewsDao(newsDatabase: NewsDatabase): SavedNewsDao {
        return newsDatabase.savedNewsDao()
    }

    @Singleton
    @Provides
    fun provideSourceDao(newsDatabase: NewsDatabase): SourceDao {
        return newsDatabase.sourceNewsDao()
    }

}
