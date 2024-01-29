package com.hardik.newsapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import com.hardik.newsapp.R
import com.hardik.newsapp.adapters.NewsAdapter
import com.hardik.newsapp.ui.ArticleActivity
import com.hardik.newsapp.ui.NewsActivity
import com.hardik.newsapp.ui.NewsViewModel

class SavedNewsFragment :Fragment(R.layout.fragment_saved_news) {

    lateinit var viewModel: NewsViewModel

    lateinit var newsAdapter: NewsAdapter
    lateinit var shimmer_layout: ShimmerFrameLayout
    lateinit var rvSavedNews: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel

        shimmer_layout = view.findViewById(R.id.shimmer_layout)
        rvSavedNews = view.findViewById(R.id.rvSavedNews)

        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
//            val bundle = Bundle().apply {
//                putSerializable("article", it)
//            }
//            findNavController().navigate(
//                R.id.action_savedNewsFragment_to_articleFragment,
//                bundle
//            )

            val intent = Intent(activity, ArticleActivity::class.java)
            intent.putExtra("article", it)
            startActivity(intent)
        }

        val itemTouchHelperCallBack = object :ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(view,"Successfully deleted article!",Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }

        }

        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(rvSavedNews)
        }

        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer {articles ->
            newsAdapter.differ.submitList(articles)
            showRecyclerView()
        })
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        rvSavedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))
        }
    }
    private fun showRecyclerView() {
        shimmer_layout.apply {
            stopShimmer()
            visibility = View.GONE
        }
        rvSavedNews.visibility = View.VISIBLE
    }
}