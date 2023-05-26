package com.app.mombaby.models.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("f_name")
    @Expose
    val firstName: String? = null,

    @SerializedName("l_name")
    @Expose
    val lastName: String? = null,

    @SerializedName("email")
    @Expose
    val email: String? = null,

    @SerializedName("age")
    @Expose
    val age: Int? = null,

    @SerializedName("job")
    @Expose
    val job: String? = null,

    @SerializedName("child_num")
    @Expose
    val childNum: Int? = null
) {
}