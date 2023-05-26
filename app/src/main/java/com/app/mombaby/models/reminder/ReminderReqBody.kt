package com.app.mombaby.models.reminder

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReminderReqBody(

    @SerializedName("id")
    @Expose
    val id: Int? = null,

    @SerializedName("title")
    @Expose
    val title: String? = "",

    @SerializedName("type")
    @Expose
    val type: String? = "",

    @SerializedName("reminder_date")
    @Expose
    var reminder_date: String? = "",

    @SerializedName("reminder_hour")
    @Expose
    val reminder_time: String? = "",

    @SerializedName("repeat_reminder")
    @Expose
    val repeat_reminder: String? = "",

    @SerializedName("reminder_period")
    @Expose
    var reminder_period: String? = "",

    @SerializedName("reminder_interval")
    @Expose
    val reminder_interval: Int = 1,
) {
}