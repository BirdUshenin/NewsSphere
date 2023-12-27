package com.birdushenin.newssphere.data.repositories

import com.birdushenin.newssphere.data.databases.daos.ArticleDao
import com.birdushenin.newssphere.data.databases.daos.SavedNewsDao
import javax.inject.Inject

class DatabaseRepository @Inject constructor(
    private val articleDao: ArticleDao,
    private val savedNewsDao: SavedNewsDao,
) {}
