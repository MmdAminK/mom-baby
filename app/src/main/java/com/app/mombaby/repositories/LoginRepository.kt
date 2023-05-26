package com.app.mombaby.repositories

import com.app.mombaby.models.Data
import com.app.mombaby.models.login.LoginDto
import com.app.mombaby.services.LoginWebService
import com.app.mombaby.services.UserAuthenticationWebService
import com.app.mombaby.utils.models.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginRepository
@Inject
constructor(
    private val loginWebService: LoginWebService,
    private val userAuthenticationWebService: UserAuthenticationWebService
) {
    fun login(mobile: String): Flow<DataState<LoginDto>> = flow {
        emit(DataState.Loading)
        try {
            emit(DataState.Success(loginWebService.login(mobile)))
        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }
    }

    fun checkAuthentication(): Flow<DataState<Data>> = flow {
        emit(DataState.Loading)
        try {
            emit(DataState.Success(userAuthenticationWebService.checkAuthentication()))
        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }
    }
}