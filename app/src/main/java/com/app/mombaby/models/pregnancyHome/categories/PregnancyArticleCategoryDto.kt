package com.app.mombaby.models.pregnancyHome.categories

import com.app.mombaby.models.pregnancyHome.PregnancyBlog
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PregnancyArticleCategoryDto(
    @SerializedName("articles_category")
    @Expose
    val articleCategory: List<PregnancyCategory>,

    @SerializedName("articles")
    @Expose
    val articles: List<PregnancyBlog>
) {
}