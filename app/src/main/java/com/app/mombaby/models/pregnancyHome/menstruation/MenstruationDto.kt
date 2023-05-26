package com.app.mombaby.models.pregnancyHome.menstruation

import com.app.mombaby.models.pregnancyHome.PregnancyBlog
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MenstruationDto(
    @SerializedName("next_ovulation_day")
    @Expose
    val nextOvulationDay: Int? = null,

    @SerializedName("next_priod_day")
    @Expose
    val nextPeriodDay: Int? = null,

    @SerializedName("today")
    @Expose
    val today: String? = null,

    @SerializedName("status")
    @Expose
    val status: String? = null,

    @SerializedName("ovulation_date")
    @Expose
    val firstOvulationDate: String? = null,

    @SerializedName("ovulation_date2")
    @Expose
    val secondOvulationDate: String? = null,

    @SerializedName("articles")
    @Expose
    val articles: List<PregnancyBlog>

)
