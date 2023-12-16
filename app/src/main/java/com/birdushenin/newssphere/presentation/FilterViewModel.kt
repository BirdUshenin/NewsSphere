package com.birdushenin.newssphere.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FilterViewModel : ViewModel() {
    private val _selectedFilter = MutableLiveData<String>()
    val selectedFilter: LiveData<String> get() = _selectedFilter

    fun setFilter(filter: String) {
        _selectedFilter.value = filter
    }
}