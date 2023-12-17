package com.birdushenin.newssphere.data

import android.widget.TextView

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