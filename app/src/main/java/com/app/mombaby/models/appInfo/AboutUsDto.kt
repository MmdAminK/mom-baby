package com.app.mombaby.models.appInfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AboutUsDto(
    @SerializedName("about")
    @Expose
    val about: List<String>
) {
}