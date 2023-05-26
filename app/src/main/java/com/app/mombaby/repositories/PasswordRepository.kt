package com.app.mombaby.repositories

import com.app.mombaby.models.Data
import com.app.mombaby.services.PasswordWebService
import com.app.mombaby.utils.models.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PasswordRepository @Inject constructor(private val passwordWebService: PasswordWebService) {

    @ExperimentalCoroutinesApi
    suspend fun addPassword(password: String): Flow<DataState<Data?>> = flow {
        emit(DataState.Loading)
        try {
            val response = passwordWebService.addPassword(password)
            if (response.isSuccessful) {
                emit(DataState.Success(response.body()))
            } else
                emit(DataState.RetrofitError(response.errorBody()?.string()))
        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }
    }

    @ExperimentalCoroutinesApi
    suspend fun checkPassword(password: String): Flow<DataState<Data?>> = flow {
        emit(DataState.Loading)
        try {
            val response = passwordWebService.checkPassword(password)
            if (response.isSuccessful) {
                emit(DataState.Success(response.body()))
            } else
                emit(DataState.RetrofitError(response.errorBody()?.string()))
        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }
    }

    @ExperimentalCoroutinesApi
    suspend fun forgetPassword(phoneNumber: String): Flow<DataState<Data?>> = flow {
        emit(DataState.Loading)
        try {
            val response = passwordWebService.forgetPassword(phoneNumber)
            if (response.isSuccessful) {
                emit(DataState.Success(response.body()))
            } else
                emit(DataState.RetrofitError(response.errorBody()?.string()))
        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }
    }

    @ExperimentalCoroutinesApi
    suspend fun forgetPasswordCheck(code: String): Flow<DataState<Data?>> = flow {
        emit(DataState.Loading)
        try {
            val response = passwordWebService.forgetPasswordCheck(code)
            if (response.isSuccessful) {
                emit(DataState.Success(response.body()))
            } else
                emit(DataState.RetrofitError(response.errorBody()?.string()))
        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }
    }

    @ExperimentalCoroutinesApi
    suspend fun updatePasswordCheck(status: Int): Flow<DataState<Data?>> = flow {
        emit(DataState.Loading)
        try {
            val response = passwordWebService.updatePasswordCheck(status)
            if (response.isSuccessful) {
                emit(DataState.Success(response.body()))
            } else
                emit(DataState.RetrofitError(response.errorBody()?.string()))
        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }
    }

}