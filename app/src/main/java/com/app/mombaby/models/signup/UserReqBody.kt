package com.app.mombaby.models.signup

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserReqBody(
    @SerializedName("f_name")
    @Expose
    val f_name: String? = null,

    @SerializedName("l_name")
    @Expose
    val l_name: String? = null,

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
    val child_num: Int? = null
) {
}