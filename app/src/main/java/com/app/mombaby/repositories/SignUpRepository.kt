package com.app.mombaby.repositories

import com.app.mombaby.models.Data
import com.app.mombaby.models.signup.UserReqBody
import com.app.mombaby.services.SignUpWebService
import com.app.mombaby.utils.models.DataState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignUpRepository @Inject constructor(private val signWebService: SignUpWebService) {

    suspend fun updateUser(userReqBody: UserReqBody): Flow<DataState<Data?>> = flow {
        emit(DataState.Loading)
        delay(1500)
        try {
            val response = signWebService.updateUser(userReqBody)
            if (response.isSuccessful)
                emit(DataState.Success(response.body()))
            else
                emit(DataState.RetrofitError(response.errorBody()?.string()))
        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }
    }
}