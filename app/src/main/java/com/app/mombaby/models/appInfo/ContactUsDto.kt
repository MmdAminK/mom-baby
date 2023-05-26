package com.app.mombaby.models.appInfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ContactUsDto(

    @SerializedName("contact")
    @Expose
    val contact: String = "",

    @SerializedName("tel")
    @Expose
    val telephoneNumber: String = "",

    @SerializedName("email")
    @Expose
    val email: String = "",

    @SerializedName("instagram")
    @Expose
    val instagram: String = "",

    @SerializedName("telegram")
    @Expose
    val telegram: String = "",

    @SerializedName("whatsapp")
    @Expose
    val whatsapp: String = "",

    @SerializedName("twitter")
    @Expose
    val twitter: String = ""

)