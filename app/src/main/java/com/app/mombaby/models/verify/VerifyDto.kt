package com.app.mombaby.models.verify

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VerifyDto(
    @SerializedName("data")
    @Expose
    val data: String? = null,

    @SerializedName("authenticateType")
    @Expose
    val authenticateType: String? = null,

    @SerializedName("access_token")
    @Expose
    val token: String? = null,

    @SerializedName("mobile")
    @Expose
    val mobile: String? = null,

    @SerializedName("name")
    @Expose
    val firstName: String? = null,

    @SerializedName("app_password")
    @Expose
    val appPassword: String? = null,

    @SerializedName("password_check")
    @Expose
    val passwordCheck: Int? = null,

    @SerializedName("error")
    @Expose
    val error: String? = null

) {
}