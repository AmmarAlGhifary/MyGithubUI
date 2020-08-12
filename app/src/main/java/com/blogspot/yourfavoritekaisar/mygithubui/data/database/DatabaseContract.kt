package com.blogspot.yourfavoritekaisar.mygithubui.data.database

import android.net.Uri
import android.provider.BaseColumns
import com.blogspot.yourfavoritekaisar.mygithubui.data.database.DatabaseContract.FavoriteUserColumns.Companion.TABLE_NAME

object DatabaseContract {
    const val AUTHORITY = "com.blogspot.yourfavoritekaisar.mygithubui"
    private const val SCHEME = "content"

    internal class FavoriteUserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite_user"
            const val _ID = "_id"
            const val LOGIN = "login"
            const val AVATAR_URL = "avatar_url"
            const val TYPE = "type"
        }
    }

    val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
        .authority(AUTHORITY)
        .appendPath(TABLE_NAME)
        .build()
}