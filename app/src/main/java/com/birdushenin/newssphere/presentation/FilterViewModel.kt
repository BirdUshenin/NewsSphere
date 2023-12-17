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

    val selectedFilter: LiveData<DataFilter> get() = _selectedFilter
    val selectedFilterPosition : LiveData<String> get() = _selectedFilterPosition

    fun setFilter(filterPopular: String?, startDate: String?, endDate: String?) {
        val data = DataFilter(
            selectedPopular = filterPopular,
            selectedCalendarStart = startDate,
            selectedCalendarEnd = endDate
        )
        _selectedFilter.value = data
    }

    fun selectFilterPosition(filter: String) {
        _selectedFilterPosition.value = filter
    }
}

