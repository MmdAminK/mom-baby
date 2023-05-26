package com.app.mombaby.repositories

import com.app.mombaby.models.user.UserDtoObj
import com.app.mombaby.services.EditProfileWebService
import com.app.mombaby.utils.models.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EditProfileRepository @Inject constructor(private val editProfileWebService: EditProfileWebService) {

    suspend fun userInfo(): Flow<DataState<UserDtoObj?>> = flow {
        emit(DataState.Loading)
        try {
            val response = editProfileWebService.userInfo()
            if (response.isSuccessful)
                emit(DataState.Success(response.body()))
            else
                emit(DataState.RetrofitError(response.errorBody()?.string()))
        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }
    }
}