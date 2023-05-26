package com.app.mombaby.models.intro

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class IntroSetting(
    @SerializedName("setting")
    @Expose
    val intro: Intro?
)
