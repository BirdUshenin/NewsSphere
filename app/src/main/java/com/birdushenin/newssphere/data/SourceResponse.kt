package com.birdushenin.newssphere.data

data class SourceResponse(
    val status: String,
    val sources: List<SourceNews>
)