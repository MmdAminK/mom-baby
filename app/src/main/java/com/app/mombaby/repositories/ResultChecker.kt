package com.app.mombaby.repositories

import com.app.mombaby.utils.models.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

object ResultChecker {

    fun <R> checkResult(response: Response<R>): Flow<DataState<R?>> = flow {
        emit(DataState.Loading)
        try {
            if (response.isSuccessful)
                emit(DataState.Success(response.body()))
            else
                emit(DataState.RetrofitError(response.errorBody()?.string()))
        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }
    }
}