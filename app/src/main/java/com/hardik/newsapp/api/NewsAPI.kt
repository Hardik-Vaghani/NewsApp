package com.hardik.newsapp.api

import com.hardik.newsapp.models.NewsResponse
import com.hardik.newsapp.util.Constants.Companion.API_KEY
import com.hardik.newsapp.util.Constants.Companion.COUNTRY_CODE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String = COUNTRY_CODE,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apikey")
        apiKey: String = API_KEY
    ):Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String = "latest",
        @Query("page")
        pageNumber: Int = 1,
        @Query("apikey")
        apiKey: String = API_KEY
    ):Response<NewsResponse>
}