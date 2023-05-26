package com.app.mombaby.models.pregnancyHome

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PregnancyHomeInfo(
    @SerializedName("child_birth_date")
    @Expose
    val childBirthDate: Int? = null,

    @SerializedName("day")
    @Expose
    val day: Int? = null,

    @SerializedName("days")
    @Expose
    val days: Int? = null,

    @SerializedName("month")
    @Expose
    val month: Int? = null,

    @SerializedName("nini")
    @Expose
    val nini: String? = null,


    @SerializedName("week_number")
    @Expose
    val week_number: Int? = null,


    @SerializedName("week")
    @Expose
    var week: String? = null,

    ) {
}