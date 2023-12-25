package com.birdushenin.newssphere.presentation.headlines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.birdushenin.newssphere.data.Article

class NewsViewModel : ViewModel() {
    private val _selectedArticle = MutableLiveData<Article?>()
    val selectedArticle: LiveData<Article?> get() = _selectedArticle

    fun selectArticle(article: Article?) {
        _selectedArticle.value = article
    }
}