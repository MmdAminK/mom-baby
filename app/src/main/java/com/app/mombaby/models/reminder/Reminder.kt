package com.app.mombaby.models.reminder

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Reminder(

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
    var reminderDate: String? = "",

    @SerializedName("reminder_hour")
    @Expose
    val reminderHour: String? = "",

    @SerializedName("reminder_time")
    @Expose
    val reminderTime: String? = "",

    @SerializedName("repeat_reminder")
    @Expose
    val repeatReminder: String? = "",

    @SerializedName("reminder_period")
    @Expose
    var reminderPeriod: String? = "",

    @SerializedName("reminder_interval")
    @Expose
    val reminderInterval: Int = 1,

    )
