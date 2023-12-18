package com.birdushenin.newssphere.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.birdushenin.newssphere.data.Article
import com.birdushenin.newssphere.data.DataFilter

class FilterViewModel : ViewModel() {
    private val _selectedFilter = MutableLiveData<DataFilter>()
    private val _selectedFilterPosition = MutableLiveData<String>()
    private val _selectedFilterPositionLang = MutableLiveData<String>()

    val selectedFilter: LiveData<DataFilter> get() = _selectedFilter
    val selectedFilterPosition: LiveData<String> get() = _selectedFilterPosition
    val selectedFilterPositionLang: LiveData<String> get() = _selectedFilterPositionLang

    fun setFilter(filterPopular: String?, startDate: String?, endDate: String?, lang: String?) {
        val data = DataFilter(
            selectedPopular = filterPopular,
            selectedCalendarStart = startDate,
            selectedCalendarEnd = endDate,
            selectedLang = lang
        )
        _selectedFilter.value = data
    }

    fun selectFilterPosition(filter: String) {
        _selectedFilterPosition.value = filter
    }

    fun selectFilterPositionLang(filter: String) {
        _selectedFilterPositionLang.value = filter
    }
}

