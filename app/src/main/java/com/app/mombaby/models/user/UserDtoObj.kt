package com.app.mombaby.models.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserDtoObj(
    @SerializedName("user")
    @Expose
    val userInfo: UserDto
) {
}