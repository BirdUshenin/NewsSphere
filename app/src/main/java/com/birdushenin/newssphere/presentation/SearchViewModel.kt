package com.birdushenin.newssphere.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel: ViewModel() {
    val searchQuery = MutableLiveData<String>()
}