package com.blogspot.yourfavoritekaisar.consumerapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.yourfavoritekaisar.consumerapp.adapter.ConsumerAdapter
import com.blogspot.yourfavoritekaisar.consumerapp.database.DatabaseContract.CONTENT_URI
import com.blogspot.yourfavoritekaisar.consumerapp.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var consumerAdapter: ConsumerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Favorite List"
        consumerAdapter = ConsumerAdapter()

        GlobalScope.launch(Dispatchers.Main) {
            val differedGit = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val fav = differedGit.await()
            if (fav.size > 0) {
                consumerAdapter.listFavorite = fav
            } else {
                consumerAdapter.listFavorite = ArrayList()
                showSnackbarMessage("data is empty")
            }
        }
        showRecyclerView()
    }

    private fun showRecyclerView() {
        rv_user.adapter = consumerAdapter
        rv_user.layoutManager = LinearLayoutManager(this)
        rv_user.setHasFixedSize(true)
    }

    private fun showSnackbarMessage(s: String) {
        Snackbar.make(rv_user, s, Snackbar.LENGTH_SHORT).show()
    }
}
