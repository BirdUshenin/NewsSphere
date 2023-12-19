package com.birdushenin.newssphere.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.birdushenin.newssphere.data.SourceNews

class SourceViewModel : ViewModel() {
    private val _selectedSource = MutableLiveData<SourceNews?>()
    val selectedSource: LiveData<SourceNews?> get() = _selectedSource

    fun selectSource(sourceNews: SourceNews?) {
        _selectedSource.value = sourceNews
    }
}