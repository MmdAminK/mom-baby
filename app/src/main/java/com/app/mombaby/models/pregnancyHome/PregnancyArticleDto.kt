package com.app.mombaby.models.pregnancyHome

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PregnancyArticleDto(
    @SerializedName("articles")
    @Expose
    val articles: List<PregnancyBlog>
) {
}