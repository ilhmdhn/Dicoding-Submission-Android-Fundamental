package com.idnsoft.githubusers.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.idnsoft.githubusers.db.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.idnsoft.githubusers.db.DatabaseContract.UserColumns.Companion.USER_ID
import com.idnsoft.githubusers.db.DatabaseContract.UserColumns.Companion._ID
import java.sql.SQLException

class UserHelper(context: Context) {

    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME

        private val INSTANCE: UserHelper? = null
        fun getInstance(context: Context): UserHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserHelper(context)
            }

        private lateinit var database: SQLiteDatabase
    }

    init {
        dataBaseHelper = DatabaseHelper(context)
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

    fun isOpen(): Boolean {
        return try {
            database.isOpen
        } catch (e: Exception) {
            false
        }
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC"
        )
    }

    fun insert(values: ContentValues): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE, null, "$USER_ID = ?", arrayOf(id), null, null, null, null
        )
    }

    fun deleteById(id: String): Int {
        return database.delete(
            DATABASE_TABLE, "$USER_ID = '$id'", null
        )
    }
}