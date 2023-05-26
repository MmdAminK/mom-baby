package com.app.mombaby.models.pregnancyHome.categories

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PregnancyGuideCategoryDto(
    @SerializedName("guides_category")
    @Expose
    val guidesCategory: List<PregnancyCategory>
) {
}