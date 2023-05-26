package com.app.mombaby.models.pregnancyHome

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PregnancyGuideDto(
    @SerializedName("guides")
    @Expose
    val guides: List<PregnancyBlog>
) {
}