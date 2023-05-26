package com.app.mombaby.services

import com.app.mombaby.models.Data
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserWebService {

    @FormUrlEncoded
    @POST("api/v1/update_user_info")
    suspend fun updateUserInfo(
        @Field("last_period") lastPeriod: String,
        @Field("time_between_period") timeBetWeenPeriod: Int,
        @Field("period_time") periodTime: Int
    ): Response<Data>

    @FormUrlEncoded
    @POST("api/v1/update_user_status")
    suspend fun updateUserStatus(@Field("status_now") status: Int): Response<Data>
}