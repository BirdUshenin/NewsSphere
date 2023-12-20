package com.birdushenin.newssphere.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.birdushenin.newssphere.data.SavedClass

class SavedViewModel : ViewModel() {
    private val _selectedArticle = MutableLiveData<List<SavedClass>>()
    val selectedArticle: LiveData<List<SavedClass>> get() = _selectedArticle

    fun selectArticle(savedClass: SavedClass) {
        val currentList = _selectedArticle.value.orEmpty().toMutableList()
        currentList.add(savedClass)
        _selectedArticle.value = currentList
    }
}
