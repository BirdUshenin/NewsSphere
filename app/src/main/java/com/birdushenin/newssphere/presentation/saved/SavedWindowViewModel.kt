package com.birdushenin.newssphere.presentation.saved

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.birdushenin.newssphere.data.SavedClass

class SavedWindowViewModel : ViewModel() {
    private val _selectedArticle = MutableLiveData<SavedClass?>()
    val selectedArticle: LiveData<SavedClass?> get() = _selectedArticle

    fun selectArticle(article: SavedClass?) {
        _selectedArticle.value = article
    }
}