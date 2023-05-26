package com.app.mombaby.models.appInfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FriendsDto(
    @SerializedName("app_ios")
    @Expose
    val iosAppLink: String = "",

    @SerializedName("app_android")
    @Expose
    val androidAppLink: String = "",
) {
}