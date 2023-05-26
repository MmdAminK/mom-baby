package com.app.mombaby.repositories

import android.content.SharedPreferences
import com.app.mombaby.models.Data
import com.app.mombaby.models.appInfo.AboutUsDto
import com.app.mombaby.models.appInfo.ContactUsDto
import com.app.mombaby.models.appInfo.FriendsDto
import com.app.mombaby.models.appInfo.RulesDto
import com.app.mombaby.services.AppInformationWebService
import com.app.mombaby.services.SendMessageWebService
import com.app.mombaby.utils.models.DataState
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AppInfoRepository @Inject constructor(
    private val appInformationWebService: AppInformationWebService,
    private val sendMessageWebService: SendMessageWebService,
    var sharedPreferences: SharedPreferences
) {

    suspend fun aboutUs(): Flow<DataState<AboutUsDto?>> = flow {
        emit(DataState.Loading)
        try {
            val response = appInformationWebService.aboutUs()
            if (response.isSuccessful) {
                sharedPreferences.edit().putString("aboutUs", response.body()!!.about[0]).apply()
                emit(DataState.Success(response.body()))
            } else
                emit(DataState.RetrofitError(response.errorBody()?.string()))
        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }
    }

    suspend fun contactUs(): Flow<DataState<ContactUsDto?>> = flow {
        emit(DataState.Loading)
        try {
            val response = appInformationWebService.contactUs()
            if (response.isSuccessful) {
                sharedPreferences.edit()
                    .putString("contactUs", Gson().toJson(response.body()?.contact!!))
                    .apply()
                emit(DataState.Success(response.body()))
            } else
                emit(DataState.RetrofitError(response.errorBody()?.string()))
        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }
    }

    suspend fun rules(): Flow<DataState<RulesDto?>> = flow {
        emit(DataState.Loading)
        try {
            val response = appInformationWebService.rules()
            if (response.isSuccessful) {
                sharedPreferences.edit()
                    .putString("rules", response.body()!!.rules.joinToString("\n"))
                    .apply()
                emit(DataState.Success(response.body()))
            } else
                emit(DataState.RetrofitError(response.errorBody()?.string()))

        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }
    }

    suspend fun friends(): Flow<DataState<FriendsDto?>> = flow {
        emit(DataState.Loading)
        try {
            val response = appInformationWebService.appLinks()
            if (response.isSuccessful) {
                sharedPreferences.edit()
                    .putString("iosAppLink", response.body()!!.iosAppLink)
                    .putString("androidAppLink", response.body()!!.androidAppLink)
                    .apply()
                emit(DataState.Success(response.body()))
            } else
                emit(DataState.RetrofitError(response.errorBody()?.string()))

        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }
    }

    fun sendMessage(name: String, phoneNumber: String, urMessage: String): Flow<DataState<Data?>> =
        flow {
            try {
                val response = sendMessageWebService.sendMessage(name, phoneNumber, urMessage)
                if (response.isSuccessful) {
                    emit(DataState.Success(response.body()))
                } else
                    emit(DataState.RetrofitError(response.errorBody()?.string()))

            } catch (exception: Exception) {
                emit(DataState.Error(exception))
            }
        }
}