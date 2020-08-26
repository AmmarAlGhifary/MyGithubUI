package com.blogspot.yourfavoritekaisar.mygithubui.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.blogspot.yourfavoritekaisar.mygithubui.data.database.DatabaseContract.AUTHORITY
import com.blogspot.yourfavoritekaisar.mygithubui.data.database.DatabaseContract.CONTENT_URI
import com.blogspot.yourfavoritekaisar.mygithubui.data.database.DatabaseContract.FavoriteUserColumns.Companion.TABLE_NAME
import com.blogspot.yourfavoritekaisar.mygithubui.data.helper.FavoriteUserHelper

class GitProvider : ContentProvider() {

    companion object {
        private const val LOGIN = 1
        private const val USER_ID = 2

        private val mUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favoriteUserHelper: FavoriteUserHelper

        init {
            mUriMatcher.addURI(AUTHORITY, TABLE_NAME, LOGIN)
            mUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", USER_ID)
        }
    }

    override fun onCreate(): Boolean {
        favoriteUserHelper = FavoriteUserHelper.getInstance(context as Context)
        favoriteUserHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val cursor: Cursor?
        when (mUriMatcher.match(uri)) {
            LOGIN -> cursor = favoriteUserHelper.queryAll()
            USER_ID -> cursor = favoriteUserHelper.queryByLogin(uri.lastPathSegment.toString())
            else -> cursor = null
        }
        return cursor
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (LOGIN) {
            mUriMatcher.match(uri) -> favoriteUserHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        TODO("Implement this to handle requests to update one or more rows.")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (USER_ID) {
            mUriMatcher.match(uri) -> favoriteUserHelper.deleteByLogin(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun getType(uri: Uri): String? {
        return null
    }
}
