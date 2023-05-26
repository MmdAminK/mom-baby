package com.app.mombaby.repositories

import com.app.mombaby.models.verify.VerifyDto
import com.app.mombaby.services.VerifyWebService
import com.app.mombaby.utils.models.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class VerifyRepository
@Inject constructor(private val verifyWebService: VerifyWebService) {

    suspend fun verify(authCode: String, mobile: String): Flow<DataState<VerifyDto?>> = flow {
        emit(DataState.Loading)
        try {
            val response = verifyWebService.verify(authCode, mobile)
            if (response.isSuccessful)
                emit(DataState.Success(response.body()))
            else
                emit(DataState.RetrofitError(response.errorBody()?.string()))
        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }
    }
}