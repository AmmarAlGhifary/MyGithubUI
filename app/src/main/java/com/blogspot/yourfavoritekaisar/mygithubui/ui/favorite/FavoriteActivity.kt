package com.blogspot.yourfavoritekaisar.mygithubui.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.yourfavoritekaisar.mygithubui.R
import com.blogspot.yourfavoritekaisar.mygithubui.adapter.FavoriteAdapter
import com.blogspot.yourfavoritekaisar.mygithubui.data.database.DatabaseContract
import com.blogspot.yourfavoritekaisar.mygithubui.data.helper.FavoriteUserHelper
import com.blogspot.yourfavoritekaisar.mygithubui.data.helper.MappingHelper
import com.blogspot.yourfavoritekaisar.mygithubui.data.model.UserFavorite
import com.blogspot.yourfavoritekaisar.mygithubui.ui.detail.DetailActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var favoriteUserHelper: FavoriteUserHelper

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_AVATAR_URL = "extra_avatar_url"
        const val EXTRA_TYPE = "extra_Type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        supportActionBar?.setTitle(R.string.favorite_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        showListFavorite()

        favoriteUserHelper = FavoriteUserHelper.getInstance(applicationContext)
        favoriteUserHelper.open()

        GlobalScope.launch(Dispatchers.Main) {
            val differedGit = async(Dispatchers.IO) {
                val cursor =
                    contentResolver?.query(DatabaseContract.CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val fav = differedGit.await()
            if (fav.size > 0) {
                favoriteAdapter.listFavoriteUser
            } else {
                favoriteAdapter.listFavoriteUser = ArrayList()
            }
            showListFavorite()
        }
        if (savedInstanceState == null) {
            loadFavoriteAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<UserFavorite>(EXTRA_USERNAME)
            if (list != null) {
                favoriteAdapter.listFavoriteUser = list
            }
        }
    }

    private fun showListFavorite() {
        rv_favorite.layoutManager = LinearLayoutManager(this)
        rv_favorite.setHasFixedSize(true)

        favoriteAdapter = FavoriteAdapter {
            val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
            intent.apply {
                putExtra(EXTRA_USERNAME, it.login)
                putExtra(EXTRA_AVATAR_URL, it.avatar_url)
            }
            startActivity(intent)
        }
        rv_favorite.adapter = favoriteAdapter
    }

    private fun loadFavoriteAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBar.visibility = View.VISIBLE
            val defferedFavorite = async(Dispatchers.IO) {
                val cursor = favoriteUserHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progressBar.visibility = View.INVISIBLE
            val favorites = defferedFavorite.await()
            if (favorites.size > 0) {
                favoriteAdapter.listFavoriteUser = favorites
            } else {
                favoriteAdapter.listFavoriteUser = ArrayList()
                Snackbar.make(rv_favorite, "Tidak ada data saat ini", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putParcelableArrayList(EXTRA_USERNAME, favoriteAdapter.listFavoriteUser)
            putParcelableArrayList(EXTRA_AVATAR_URL, favoriteAdapter.listFavoriteUser)
            putParcelableArrayList(EXTRA_TYPE, favoriteAdapter.listFavoriteUser)
        }
    }

    override fun onResume() {
        super.onResume()
        showListFavorite()
        loadFavoriteAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteUserHelper.close()
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }
}