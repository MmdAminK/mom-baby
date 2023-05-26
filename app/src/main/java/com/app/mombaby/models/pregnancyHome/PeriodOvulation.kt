package com.app.mombaby.models.pregnancyHome

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PeriodOvulation(
    @SerializedName("period")
    @Expose
    val periods: List<String>,

    @SerializedName("ovulation")
    @Expose
    val ovulation: List<String>
) {
}