package com.birdushenin.newssphere.domain

import com.birdushenin.newssphere.data.SourceNews

interface OnSourceItemWindowClicker {
    fun onNewsItemWindowClicked(sourceNews: SourceNews)
}