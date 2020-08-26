package com.blogspot.yourfavoritekaisar.mygithubui.ui.detail

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.yourfavoritekaisar.mygithubui.R
import com.blogspot.yourfavoritekaisar.mygithubui.adapter.DetailAdapter
import com.blogspot.yourfavoritekaisar.mygithubui.adapter.PagerAdapter
import com.blogspot.yourfavoritekaisar.mygithubui.data.database.DatabaseContract
import com.blogspot.yourfavoritekaisar.mygithubui.data.database.DatabaseContract.FavoriteUserColumns.Companion.AVATAR_URL
import com.blogspot.yourfavoritekaisar.mygithubui.data.database.DatabaseContract.FavoriteUserColumns.Companion.LOGIN
import com.blogspot.yourfavoritekaisar.mygithubui.data.helper.FavoriteUserHelper
import com.blogspot.yourfavoritekaisar.mygithubui.data.model.UserFavorite
import com.blogspot.yourfavoritekaisar.mygithubui.ui.settings.AlarmActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.tab_layout.*


class DetailActivity : AppCompatActivity() {

    private lateinit var detailViewModel: DetailViewModel

    private lateinit var detailAdapter: DetailAdapter
    private lateinit var favoriteUserHelper: FavoriteUserHelper

    private lateinit var login: String
    private lateinit var avatarUrl: String
    private lateinit var type: String

    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false
    private var userFavorite: UserFavorite? = null
    private lateinit var uriWithId: Uri

    companion object {
        internal val TAG = DetailActivity::class.java.simpleName
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_AVATAR_URL = "extra_avatar_url"
        const val EXTRA_TYPE = "extra_type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initToolbar()
        favoriteUserHelper = FavoriteUserHelper.getInstance(applicationContext)
        favoriteUserHelper.open()

        detailAdapter = DetailAdapter()
        rv_detail.layoutManager = LinearLayoutManager(this)
        rv_detail.adapter = detailAdapter

        val pagerAdapter = PagerAdapter(this, supportFragmentManager)
        viewPager.adapter = pagerAdapter
        tab_layout.setupWithViewPager(viewPager)

        val username = intent.getStringExtra(EXTRA_USERNAME)

        detailViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailViewModel::class.java)

        detailViewModel.setDetail(username!!)
        detailViewModel.getDetailUser().observe(this, Observer { detailItemUser ->
            if (detailItemUser != null) {
                detailAdapter.setData(detailItemUser)
            }
        })

        favoriteState()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        menuItem = menu
        setFavorite()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_alarm -> {
                val intent = Intent(this, AlarmActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_favorite -> {
                if (isFavorite) removeFavoriteUser() else addFavoriteUser()
                isFavorite = !isFavorite
                setFavorite()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

//    private fun addFavoriteUser(){
//        if (isFavorite) {
//            userFavorite?.let {
//                contentResolver.delete(uriWithId, null, null)
//                Log.d(TAG, "Favorite Deleted")
//                showSnackbarMessage("${it.login} Unfavorite")
//                menuItem?.let {
//                    isFavorite = false
//                    setFavorite()
//                }
//            }
//        } else {
//            val values = ContentValues()
//            values.put(LOGIN, userFavorite?.login)
//            values.put(AVATAR_URL, userFavorite?.avatar_url)
//            contentResolver.insert(CONTENT_URI, values)
//            userFavorite?.login
//            Log.d(TAG, "Favorite Added")
//            showSnackbarMessage("Added ${userFavorite?.login} to Favorite")
//            menuItem?.let {
//                isFavorite = true
//                setFavorite()
//            }
//        }
//    }

    private fun addFavoriteUser() {
        try {
            login = intent?.getStringExtra(EXTRA_USERNAME).toString()
            avatarUrl = intent?.getStringExtra(EXTRA_AVATAR_URL).toString()
            type = intent?.getStringExtra(EXTRA_TYPE).toString()

            val values = ContentValues().apply {
                put(LOGIN, login)
                put(AVATAR_URL, avatarUrl)
                put(DatabaseContract.FavoriteUserColumns.TYPE, type)
            }
            favoriteUserHelper.insert(values)

            showSnackbarMessage("Added to favorite")
            Log.d("INSERT VALUES ::::: ", values.toString())
        } catch (e: SQLiteConstraintException) {
            showSnackbarMessage("" + e.localizedMessage)
        }
    }

    private fun removeFavoriteUser() {
        try {
            login = intent?.getStringExtra(EXTRA_USERNAME).toString()
            val result = favoriteUserHelper.deleteByLogin(login)

            showSnackbarMessage("Removed to favorite")
            Log.d("REMOVE VALUES ::::: ", result.toString())
        } catch (e: SQLiteConstraintException) {
            showSnackbarMessage("" + e.localizedMessage)
        }
    }

    private fun favoriteState() {
        login = intent?.getStringExtra(EXTRA_USERNAME).toString()
        val result = favoriteUserHelper.queryByLogin(login)
        val favorite = (1..result.count).map {
            result.apply {
                moveToNext()
                getInt(result.getColumnIndexOrThrow(LOGIN))
            }
        }
        isFavorite = favorite.isNotEmpty()
    }

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon =
                ContextCompat.getDrawable(this, R.drawable.ic_added_favorite)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun showSnackbarMessage(s: String) {
        Snackbar.make(viewPager, s, Snackbar.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun initToolbar() {
        val action = supportActionBar
        action!!.title = resources.getString(R.string.title_detail)
        action.setDisplayHomeAsUpEnabled(true)
    }
}



