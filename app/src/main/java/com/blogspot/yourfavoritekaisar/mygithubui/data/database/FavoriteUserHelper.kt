package com.blogspot.yourfavoritekaisar.mygithubui.data.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.blogspot.yourfavoritekaisar.mygithubui.data.database.DatabaseContract.FavoriteUserColumns.Companion.LOGIN
import com.blogspot.yourfavoritekaisar.mygithubui.data.database.DatabaseContract.FavoriteUserColumns.Companion.TABLE_NAME

class FavoriteUserHelper(context: Context) {

    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {

        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: FavoriteUserHelper? = null

        fun getInstance(context: Context): FavoriteUserHelper = INSTANCE ?: synchronized(this) {
            INSTANCE ?: FavoriteUserHelper(context)
        }
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "${BaseColumns._ID} ASC",
            null
        )
    }

    fun queryByLogin(login: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$LOGIN = ?",
            arrayOf(login),
            null,
            null,
            null,
            null
        )
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun deleteByLogin(login: String): Int {
        return database.delete(DATABASE_TABLE, "$LOGIN = '$login'", null)
    }
}