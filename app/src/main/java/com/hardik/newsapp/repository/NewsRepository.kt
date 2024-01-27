package com.hardik.newsapp.repository

import android.util.Log
import com.hardik.newsapp.api.RetrofitInstance
import com.hardik.newsapp.db.ArticleDatabase
import com.hardik.newsapp.models.Article
import com.hardik.newsapp.models.NewsResponse
import retrofit2.Response

class NewsRepository(val db: ArticleDatabase) {
    val TAG = NewsRepository::class.java.simpleName

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int): Response<NewsResponse> {
        Log.d(TAG, "getBreakingNews: $pageNumber")
        return RetrofitInstance.api.getBreakingNews(
            countryCode = countryCode,
            pageNumber = pageNumber
        )
    }

    suspend fun searchNews(searchQuery: String, pageNumber: Int): Response<NewsResponse> {
        Log.d(TAG, "searchNews: $pageNumber")
        return RetrofitInstance.api.searchForNews(searchQuery = searchQuery, pageNumber = pageNumber)
    }

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

    fun containsArticle(title: String, content: String) = db.getArticleDao().containsArticle(title,content)
    fun containsArticle(title: String) = db.getArticleDao().containsArticle(title)

}