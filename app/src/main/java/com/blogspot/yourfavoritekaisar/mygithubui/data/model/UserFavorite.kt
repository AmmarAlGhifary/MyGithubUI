package com.blogspot.yourfavoritekaisar.mygithubui.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserFavorite(
    var id: Int = 0,
    var login: String? = null,
    var avatar_url: String? = null
) : Parcelable