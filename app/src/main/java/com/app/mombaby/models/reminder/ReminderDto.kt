package com.app.mombaby.models.reminder

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReminderDto(

    @SerializedName("reminders")
    @Expose
    var reminders: ArrayList<Reminder?> = ArrayList()
)