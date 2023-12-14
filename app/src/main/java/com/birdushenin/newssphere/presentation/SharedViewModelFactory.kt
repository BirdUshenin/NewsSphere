package com.birdushenin.newssphere.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SharedViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewsViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}