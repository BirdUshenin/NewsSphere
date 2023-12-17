package com.birdushenin.newssphere.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FilterViewModel : ViewModel() {
    private val _selectedFilter = MutableLiveData<String>()
    private val _selectedCalendarStart = MutableLiveData<String>()
    private val _selectedCalendarEnd = MutableLiveData<String>()

    val selectedFilter: LiveData<String> get() = _selectedFilter
    val selectedCalendarStart: LiveData<String> get() = _selectedCalendarStart
    val selectedCalendarEnd: LiveData<String> get() = _selectedCalendarEnd

    private val _combinedLiveData = MediatorLiveData<Triple<String?, String?, String?>>()

    init {
        _combinedLiveData.addSource(_selectedFilter) {
            _combinedLiveData.value =
                Triple(it, _selectedCalendarStart.value, _selectedCalendarEnd.value)
        }

        _combinedLiveData.addSource(_selectedCalendarStart) {
            _combinedLiveData.value = Triple(_selectedFilter.value, it, _selectedCalendarEnd.value)
        }

        _combinedLiveData.addSource(_selectedCalendarEnd) {
            _combinedLiveData.value =
                Triple(_selectedFilter.value, _selectedCalendarStart.value, it)
        }
    }

    val combinedLiveData: LiveData<Triple<String?, String?, String?>> get() = _combinedLiveData

    fun setFilter(filter: String) {
        _selectedFilter.value = filter
    }

    fun setStart(startDate: String) {
        _selectedCalendarStart.value = startDate
    }

    fun setEnd(endDate: String) {
        _selectedCalendarEnd.value = endDate
    }
}