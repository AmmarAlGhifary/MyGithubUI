package com.blogspot.yourfavoritekaisar.mygithubui.data.helper

import android.database.Cursor
import com.blogspot.yourfavoritekaisar.mygithubui.data.database.DatabaseContract
import com.blogspot.yourfavoritekaisar.mygithubui.data.model.UserFavorite
import java.util.*

object MappingHelper {

    fun mapCursorToArrayList(favoriteUserCursor: Cursor?): ArrayList<UserFavorite> {
        val favoriteUserItemsList = ArrayList<UserFavorite>()

        favoriteUserCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns._ID))
                val login =
                    getString(getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.LOGIN))
                val avatarUrl =
                    getString(getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.AVATAR_URL))
                favoriteUserItemsList.add(UserFavorite(id, login, avatarUrl))
            }
        }
        return favoriteUserItemsList
    }
}