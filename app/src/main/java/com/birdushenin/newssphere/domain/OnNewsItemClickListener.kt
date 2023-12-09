package com.birdushenin.newssphere.domain

import com.birdushenin.newssphere.data.Article

interface OnNewsItemClickListener {
    fun onNewsItemClicked(article: Article)
}