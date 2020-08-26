package com.blogspot.yourfavoritekaisar.consumerapp.helper

import android.database.Cursor
import com.blogspot.yourfavoritekaisar.consumerapp.database.DatabaseContract
import com.blogspot.yourfavoritekaisar.consumerapp.model.User
import java.util.*

object MappingHelper {
    val TAG = MappingHelper::class.java.simpleName
    fun mapCursorToArrayList(favoriteUserCursor: Cursor?): ArrayList<User> {
        val favoriteUserItemsList = ArrayList<User>()
        favoriteUserCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns._ID))
                val login =
                    getString(getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.LOGIN))
                val avatarUrl =
                    getString(getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.AVATAR_URL))
                favoriteUserItemsList.add(User(id, login, avatarUrl))
            }
        }
        return favoriteUserItemsList
    }
}