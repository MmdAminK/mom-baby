package com.app.mombaby.models.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginDto(
    @SerializedName("authenticateType")
    @Expose
    val authenticateType: String? = null,

    @SerializedName("mobile")
    @Expose
    val mobile: String? = null,
) {
}