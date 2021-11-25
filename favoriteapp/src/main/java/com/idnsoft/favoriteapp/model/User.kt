package com.idnsoft.favoriteapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (
    var userId: Int? = null,
    var dbId: Int? = null,
    var userName: String? = null,
    var name: String? = null,
    var photo: String? = null,
    var email: String? = null,
    var bio: String? = null,
    var url: String? = null,
    var followingPhoto: String? = null,
    var followingName: String? = null,
    var followersName: String? = null,
    var followersPhoto: String? = null
) : Parcelable