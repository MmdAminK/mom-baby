package com.app.mombaby.repositories

import com.app.mombaby.database.MomBabyDb
import com.app.mombaby.models.Data
import com.app.mombaby.models.user.User
import com.app.mombaby.models.user.UserEntity
import com.app.mombaby.services.UserWebService
import com.app.mombaby.utils.models.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepository @Inject constructor(private val userWebService: UserWebService) {
    @Inject
    lateinit var appDb: MomBabyDb

    private var user = User()

    suspend fun insertUser(user: UserEntity) {
        //add mapper appDb.userDao().insertUser(Mapper(UserEntity()))
//        appDb.userDao().insertUser(User())
    }

    suspend fun user() {
        //add mapper user = Mapper(appDb.userDao().findUser())
//        appDb.userDao().findUser()
    }

    fun sendUserInfo(
        lastPeriod: String, timeBetWeenPeriod: Int, periodTime: Int
    ): Flow<DataState<Data?>> = flow {
        emit(DataState.Loading)
        try {
            val response = userWebService.updateUserInfo(lastPeriod, timeBetWeenPeriod, periodTime)
            if (response.isSuccessful) {
                emit(DataState.Success(response.body()))
            } else {
                emit(DataState.RetrofitError(response.errorBody()?.string()))
            }
        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }
    }

    fun updateUserStatus(status: Int): Flow<DataState<Data?>> = flow {
        emit(DataState.Loading)
        try {
            val response = userWebService.updateUserStatus(status)
            if (response.isSuccessful) {
                emit(DataState.Success(response.body()))
            } else {
                emit(DataState.RetrofitError(response.errorBody()?.string()))
            }
        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }
    }

    fun firstName(): String? = user.firstName
    fun lastName(): String? = user.lastName
    fun phoneNumber(): String? = user.phoneNumber
    fun email(): String? = user.email
    fun age(): Int? = user.age
    fun childNum(): Int? = user.childNum

}