package com.app.mombaby.models.appInfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RulesDto(
    @SerializedName("rules")
    @Expose
    val rules: List<String>
) {
}