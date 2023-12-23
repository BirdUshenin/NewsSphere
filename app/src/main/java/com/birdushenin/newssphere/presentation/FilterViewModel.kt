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

    var xValue: Int = 0
    var yValue: Int = 0
    var zValue: Int = 0

    private val _appliedFilterCount = MutableLiveData<Int>()
    val appliedFilterCount: LiveData<Int>
        get() = _appliedFilterCount

    private val _filterCountEvent = MutableLiveData<Unit>()
    val filterCountEvent: LiveData<Unit>
        get() = _filterCountEvent

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

    fun incrementFilterCount() {
        val currentCount = _appliedFilterCount.value ?: 0
        _appliedFilterCount.value = currentCount + 1
        _filterCountEvent.value = Unit
    }
}
