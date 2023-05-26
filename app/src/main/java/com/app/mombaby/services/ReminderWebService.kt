package com.app.mombaby.services

import com.app.mombaby.models.Data
import com.app.mombaby.models.reminder.ReminderData
import com.app.mombaby.models.reminder.ReminderDto
import com.app.mombaby.models.reminder.ReminderReqBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReminderWebService {

    @POST("api/v1/insert_reminder")
    suspend fun addReminder(@Body reminder: ReminderReqBody): Response<ReminderData>

    @GET("api/v1/delete_reminder/{id}")
    suspend fun removeReminder(@Path("id") reminderId: Int): Response<Data>


    @GET("api/v1/get_reminder")
    suspend fun reminders(): Response<ReminderDto?>

    @POST("api/v1/update_reminder")
    suspend fun editReminder(@Body reminder: ReminderReqBody): Response<Data>
}