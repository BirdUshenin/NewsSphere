package com.birdushenin.newssphere.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.birdushenin.newssphere.data.FilterState

class CalendarViewModel : ViewModel() {
    private val _filterState = MutableLiveData<FilterState>()

    val filterState: LiveData<FilterState> get() = _filterState

    fun setFilterState(state: FilterState) {
        _filterState.value = state
    }
}