package com.hardik.bottomenavigation.ui.fragments

import android.app.ActionBar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.hardik.bottomenavigation.R
import com.hardik.bottomenavigation.models.Article
import com.hardik.bottomenavigation.ui.NewsActivity
import com.hardik.bottomenavigation.ui.NewsViewModel

class ArticleFragment : Fragment(R.layout.fragment_article) {
    val TAG = ArticleFragment::class.java.simpleName

    lateinit var viewModel: NewsViewModel
    lateinit var webView: WebView
    lateinit var fab: FloatingActionButton
    var isBottomNavigationViewVisible = true
    val args: ArticleFragmentArgs by navArgs()
//    private var article:Article? = null

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            article = it.getSerializable("article") as? Article
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        webView = view.findViewById(R.id.webView)
        fab = view.findViewById(R.id.fab)

        val article: Article = args.article
        webView.apply {
            Log.e(TAG, "Article title: ${article.title + "\n Article url: " + article.url}")
            webChromeClient = WebChromeClient()
            article.url?.let { loadUrl(it) }
        }

        fab.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view, "Article saved successfully!", Snackbar.LENGTH_SHORT).show()
        }

        webView.setOnScrollChangeListener(object : View.OnScrollChangeListener {
            override fun onScrollChange(
                v: View?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (scrollY > oldScrollY && isBottomNavigationViewVisible) {
                    //Scrolling down, hide the toolbar
                   hideBottomNavigationView()
                } else if (scrollY < oldScrollY && !isBottomNavigationViewVisible) {
                    //Scrolling up, show the toolbar
                    showBottomNavigationView()
                }
            }
        })
    }

    private fun hideBottomNavigationView() {
        (requireActivity() as NewsActivity).navView.animate().translationY((requireActivity() as NewsActivity).navView.height.toFloat()).setDuration(200).start()
        isBottomNavigationViewVisible = false
    }

    private fun showBottomNavigationView() {
        (requireActivity() as NewsActivity).navView.animate().translationY(0f).setDuration(200).start()
        isBottomNavigationViewVisible = true
    }
}