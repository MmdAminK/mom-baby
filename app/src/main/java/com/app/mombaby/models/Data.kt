package com.app.mombaby.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("data")
    @Expose
    val data: String? = null
) {
}