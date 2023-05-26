package com.app.mombaby.repositories

import com.app.mombaby.models.Data
import com.app.mombaby.models.reminder.ReminderData
import com.app.mombaby.models.reminder.ReminderDto
import com.app.mombaby.models.reminder.ReminderReqBody
import com.app.mombaby.services.ReminderWebService
import com.app.mombaby.utils.models.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ReminderRepository @Inject constructor(private val reminderWebService: ReminderWebService) {

    @ExperimentalCoroutinesApi
    suspend fun reminders(): Flow<DataState<ReminderDto?>> = channelFlow {
        send(DataState.Loading)
        try {
            val response = reminderWebService.reminders()
            if (response.isSuccessful) {
                send(DataState.Success(response.body()))
            } else
                send(DataState.RetrofitError(response.errorBody()?.string()))
        } catch (exception: Exception) {
            send(DataState.Error(exception))
        }
    }

    suspend fun addReminder(reminder: ReminderReqBody): Flow<DataState<ReminderData?>> = flow {
        emit(DataState.Loading)
        try {
            val response = reminderWebService.addReminder(reminder)
            if (response.isSuccessful)
                emit(DataState.Success(response.body()))
            else
                emit(DataState.RetrofitError(response.errorBody()?.string()))
        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }
    }

    suspend fun editReminder(reminder: ReminderReqBody): Flow<DataState<Data?>> = flow {
        emit(DataState.Loading)
        try {
            val response = reminderWebService.editReminder(reminder)
            if (response.isSuccessful)
                emit(DataState.Success(response.body()))
            else
                emit(DataState.RetrofitError(response.errorBody()?.string()))
        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }
    }

    fun removeReminder(reminderId: Int): Flow<DataState<Data?>> = flow {
        emit(DataState.Loading)
        try {
            val response = reminderWebService.removeReminder(reminderId)
            if (response.isSuccessful)
                emit(DataState.Success(response.body()))
            else
                emit(DataState.RetrofitError(response.errorBody()?.string()))
        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }

    }
}