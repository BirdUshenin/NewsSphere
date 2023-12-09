package com.birdushenin.newssphere.data

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)