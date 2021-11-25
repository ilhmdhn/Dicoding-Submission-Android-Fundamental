package com.idnsoft.githubusers.helper

import android.database.Cursor
import com.idnsoft.githubusers.db.DatabaseContract
import com.idnsoft.githubusers.model.User

object MappingHelper {

    fun mapCursorToArrayList(userCursor: Cursor?): ArrayList<User> {
        val userFavList = ArrayList<User>()

        userCursor?.apply {
            while (moveToNext()) {
                val user = User()
                user.dbId = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                user.userId = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns.USER_ID))
                user.userName =
                    getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
                user.photo =
                    getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR_URL))
                userFavList.add(user)
            }
        }
        return userFavList
    }
}