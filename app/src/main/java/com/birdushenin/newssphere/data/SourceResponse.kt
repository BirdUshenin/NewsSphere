package com.birdushenin.newssphere.data

data class SourceResponse(
    val status: String,
    val totalResults: Int,
    val sources: List<SourceNews>
)