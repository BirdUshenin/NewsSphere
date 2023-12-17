package com.birdushenin.newssphere.presentation

import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class FilterState(
    val startDate: String,
    val endDate: String,
    val chooseDate: TextView,
    val textColor: Int,
    val imageResource: Int,
    val imageWidth: Int,
    val imageHeight: Int,
    val density: Float,
    val newWidthInDp: Int,
    val newHeightInDp: Int
)

class CalendarViewModel : ViewModel() {
    private val _filterState = MutableLiveData<FilterState>()

    val filterState: LiveData<FilterState> get() = _filterState

    fun setFilterState(state: FilterState) {
        _filterState.value = state
    }
}