package com.birdushenin.newssphere.presentation.headlines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel: ViewModel() {
    val searchQuery = MutableLiveData<String>()
}