package com.blogspot.yourfavoritekaisar.mygithubui.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.blogspot.yourfavoritekaisar.mygithubui.data.database.DatabaseContract.FavoriteUserColumns.Companion.AVATAR_URL
import com.blogspot.yourfavoritekaisar.mygithubui.data.database.DatabaseContract.FavoriteUserColumns.Companion.LOGIN
import com.blogspot.yourfavoritekaisar.mygithubui.data.database.DatabaseContract.FavoriteUserColumns.Companion.TABLE_NAME
import com.blogspot.yourfavoritekaisar.mygithubui.data.database.DatabaseContract.FavoriteUserColumns.Companion.TYPE
import com.blogspot.yourfavoritekaisar.mygithubui.data.database.DatabaseContract.FavoriteUserColumns.Companion._ID

internal class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "dbgithubapp"

        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_FAVORITE_USER = "CREATE TABLE $TABLE_NAME" +
                " ($_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " $LOGIN TEXT NOT NUll," +
                " $AVATAR_URL TEXT NOT NULL," +
                " $TYPE TEXT NOT NULL"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(SQL_CREATE_TABLE_FAVORITE_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}