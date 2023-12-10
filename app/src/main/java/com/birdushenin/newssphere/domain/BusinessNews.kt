package com.birdushenin.newssphere.domain

import com.birdushenin.newssphere.data.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface BusinessNews {
    @Headers("Content-Type: application/json")
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "us",
        @Query("category") category: String = "business",
        @Query("apiKey") apiKey: String
    ): Response<NewsResponse>
}