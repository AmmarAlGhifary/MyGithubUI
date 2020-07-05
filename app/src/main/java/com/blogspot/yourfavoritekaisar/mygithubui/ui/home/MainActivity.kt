package com.blogspot.yourfavoritekaisar.mygithubui.ui.home

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.yourfavoritekaisar.mygithubui.R
import com.blogspot.yourfavoritekaisar.mygithubui.ui.favorite.FavoriteActivity
import com.blogspot.yourfavoritekaisar.mygithubui.ui.settings.AlarmActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var userAdapter: com.blogspot.yourfavoritekaisar.mygithubui.adapter.ListAdapter

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Home"
        userAdapter = com.blogspot.yourfavoritekaisar.mygithubui.adapter.ListAdapter()

        rv_user.setHasFixedSize(true)
        rv_user.layoutManager = LinearLayoutManager(this)
        rv_user.adapter = userAdapter

        userViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(UserViewModel::class.java)

        userViewModel.getUser().observe(this, Observer { userItems ->
            if (userItems != null) {
                userAdapter.setData(userItems)
                showProgress(false)
            }
        })
    }

    @Suppress("SameParameterValue")
    private fun showProgress(b: Boolean) {
        if (b) {
            progressBarItem.visibility = View.VISIBLE
        } else {
            progressBarItem.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu!!.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                userViewModel.setUser(query.toString())
                showLoading(true)
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    @Suppress("SameParameterValue")
    private fun showLoading(b: Boolean) {
        if (b) {
            progressBarItem.visibility = View.VISIBLE
        } else {
            progressBarItem.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.alarm -> {
                val intent = Intent(this, AlarmActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.main_favorite -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
