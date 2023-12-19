package com.birdushenin.newssphere.domain

import com.birdushenin.newssphere.data.NewsResponse
import com.birdushenin.newssphere.data.SourceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NewsService {
    @Headers("Content-Type: application/json")
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "us",
        @Query("category") category: String = "general",
        @Query("sortBy") sortBy: String = "popular",
        @Query("apiKey") apiKey: String
    ): Response<NewsResponse>

    @Headers("Content-Type: application/json")
    @GET("everything")
    suspend fun getEverything(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String,
        @Query("from") from: String? = null,
        @Query("to") to: String? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("language") language: String? = null
    ): Response<NewsResponse>

    @GET("/v2/sources")
    suspend fun getSources(
        @Query("apiKey") apiKey: String,
        @Query("id") id: String? = null,
        @Query("source") source: String? = null
    ): Response<SourceResponse>

    @Headers("Content-Type: application/json")
    @GET("everything")
    suspend fun getRelevant(
        @Query("q") query: String? = null,
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("language") language: String = "en",
        @Query("apiKey") apiKey: String
    ): Response<NewsResponse>

    @Headers("Content-Type: application/json")
    @GET("top-headlines")
    suspend fun getSourcesNews(
        @Query("sources") source: String? = null,
        @Query("apiKey") apiKey: String
    ): Response<NewsResponse>
}

