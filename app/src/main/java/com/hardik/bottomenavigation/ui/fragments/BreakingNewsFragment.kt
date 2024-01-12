package com.hardik.bottomenavigation.ui.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.NumberPicker.OnScrollListener
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hardik.bottomenavigation.R
import com.hardik.bottomenavigation.adapters.NewsAdapter
import com.hardik.bottomenavigation.models.Article
import com.hardik.bottomenavigation.models.Source
import com.hardik.bottomenavigation.ui.ArticleActivity
import com.hardik.bottomenavigation.ui.NewsActivity
import com.hardik.bottomenavigation.ui.NewsViewModel
import com.hardik.bottomenavigation.util.Constants.Companion.COUNTRY_CODE
import com.hardik.bottomenavigation.util.Constants.Companion.QUERY_PAGE_SIZE
import com.hardik.bottomenavigation.util.Resource

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {
    val TAG = BreakingNewsFragment::class.java.simpleName

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var rvBreakingNews: RecyclerView
    lateinit var paginationProgressBar: ProgressBar

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        rvBreakingNews = view.findViewById(R.id.rvBreakingNews)
        paginationProgressBar = view.findViewById(R.id.paginationProgressBar)

        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
//            val bundle = Bundle().apply {
//                putSerializable("article", article)
//            }
//            findNavController().navigate(R.id.articleFragment,bundle)
//            val value = bundle.getSerializable("article") as? Article


//            findNavController().navigate(BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(it))
//            findNavController().navigate(R.id.action_breakingNewsFragment_to_articleFragment, Bundle().apply { putSerializable("article", it)as? Article})

            val intent = Intent(activity, ArticleActivity::class.java)
            intent.putExtra("article", it)
            startActivity(intent)
        }

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPage = newsResponse.totalResults / QUERY_PAGE_SIZE * 2
                        isLastPage = viewModel.breakingNewsPage == totalPage
                        if (isLastPage) {
                            rvBreakingNews.setPadding(0, 0, 0, 0)
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occurred $message")
                        Toast.makeText(activity, "An error occurred $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotABeginning = firstVisibleItemPosition >= 0
            val totalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotABeginning && totalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                Log.e(TAG, "onScrolled: ")
                viewModel.getBreakingNews(COUNTRY_CODE)
                isScrolling = false
            }
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
            addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))
        }
    }
}
