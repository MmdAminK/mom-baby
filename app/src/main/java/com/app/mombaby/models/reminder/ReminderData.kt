package com.app.mombaby.models.reminder

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReminderData(
    @SerializedName("data")
    @Expose
    val data: String? = null,

    @SerializedName("reminder_id")
    @Expose
    val reminderId: Int? = null
) {

}