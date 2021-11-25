package com.idnsoft.favoriteapp.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.idnsoft.githubusers"
    const val SCHEME = "content"

    class UserColumns : BaseColumns {

        companion object{
            const val TABLE_NAME = "favorite_user"
            const val USER_ID = "user_id"
            const val _ID = "_id"
            const val USERNAME= "username"
            const val AVATAR_URL= "avatar_url"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_NAME)
            .build()
        }

    }
}