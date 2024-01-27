package com.hardik.newsapp.ui

import android.graphics.PorterDuff
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.hardik.newsapp.R
import com.hardik.newsapp.databinding.ActivityArticleBinding
import com.hardik.newsapp.db.ArticleDatabase
import com.hardik.newsapp.models.Article
import com.hardik.newsapp.repository.NewsRepository

class ArticleActivity : AppCompatActivity() {
    val TAG = ArticleActivity::class.java.simpleName
    lateinit var binding: ActivityArticleBinding
    lateinit var viewModel: NewsViewModel

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application, newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val article = intent.getSerializableExtra("article") as? Article
        if (article != null) {
            viewModel.containsArticle(article.title!!, article.content!!).observe(this@ArticleActivity, Observer<Boolean> { isContainsArticle ->
                if(isContainsArticle)
                binding.favImg.setColorFilter(ContextCompat.getColor(this, android.R.color.holo_red_dark), PorterDuff.Mode.SRC_IN)
                else
                binding.favImg.setColorFilter(ContextCompat.getColor(this, R.color.teal_700), PorterDuff.Mode.SRC_IN)
            })

            binding.webView.apply {
                Log.e(TAG, "Article title: ${article?.title + "\n Article url: " + article?.url}")
                webChromeClient = WebChromeClient()
                article?.url?.let { loadUrl(it) }
            }

            binding.fab.setOnClickListener {
                viewModel.saveArticle(article)
                Snackbar.make(it, "Article saved successfully!", Snackbar.LENGTH_SHORT).show()
            }

            binding.favImg.setOnClickListener {
                viewModel.saveArticle(article)
                Snackbar.make(it, "Article saved successfully!", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}