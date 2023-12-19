package com.birdushenin.newssphere.domain

import com.birdushenin.newssphere.data.SourceNews

interface OnSourceItemClickListener {
    fun onNewsItemClicked(sourceNews: SourceNews)
}