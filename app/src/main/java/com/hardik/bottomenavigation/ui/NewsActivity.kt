package com.hardik.bottomenavigation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ActionBarContainer
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.AppBarLayout
import com.hardik.bottomenavigation.R
import com.hardik.bottomenavigation.db.ArticleDatabase
import com.hardik.bottomenavigation.repository.NewsRepository

class NewsActivity : AppCompatActivity() {
    val TAG = NewsActivity::class.java.simpleName
    lateinit var viewModel: NewsViewModel
    lateinit var navView: BottomNavigationView
    lateinit var my_toolbar: Toolbar
    lateinit var appBarLayout: AppBarLayout
    lateinit var container: ConstraintLayout
    var isToolbarVisible = true
    var isBottomNavigationViewVisible = true

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application, newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        setContentView(R.layout.activity_news)
        appBarLayout = findViewById(R.id.appBarLayout)
        my_toolbar = findViewById(R.id.my_toolbar)
        container = findViewById(R.id.container)
        setSupportActionBar(my_toolbar)

        navView = findViewById(R.id.nav_view)
//        navView.setupWithNavController(findNavController(R.id.nav_host_fragment_activity_news))
        val navController = findNavController(R.id.nav_host_fragment_activity_news)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.breakingNewsFragment,
                R.id.savedNewsFragment,
                R.id.searchNewsFragment,
                R.id.articleFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Handle the scroll change to show/hide the Toolbar and BottomNavigationView
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            val maxScroll = appBarLayout.totalScrollRange
            val percentage = Math.abs(verticalOffset).toFloat() / maxScroll.toFloat()

            if (percentage >= 0.7f && isToolbarVisible) {
                // Toolbar is fully collapsed, hide it
                hideToolbarAndBottomNavigationView()
            } else if (percentage < 0.7f && !isToolbarVisible) {
                // Toolbar is not fully collapsed, show it
                showToolbarAndBottomNavigationView()
            }
        })
    }

    private fun hideToolbarAndBottomNavigationView() {
        my_toolbar.animate().translationY(-my_toolbar.height.toFloat()).setDuration(200).start()
        navView.animate().translationY(navView.height.toFloat()).setDuration(200).start()
        isToolbarVisible = false
        isBottomNavigationViewVisible = false
    }

    private fun showToolbarAndBottomNavigationView() {
        my_toolbar.animate().translationY(0f).setDuration(200).start()
        navView.animate().translationY(0f).setDuration(200).start()
        isToolbarVisible = true
        isBottomNavigationViewVisible = true
    }
}

//https://www.youtube.com/playlist?list=PLQkwcJG4YTCRF8XiCRESq1IFFW8COlxYJ
//https://newsapi.org/


//app:layout_scrollFlags="scroll|enterAlways|snap"
//app:layout_behavior="@string/appbar_scrolling_view_behavior"

