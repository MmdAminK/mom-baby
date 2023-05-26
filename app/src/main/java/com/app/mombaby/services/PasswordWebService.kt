package com.app.mombaby.services

import com.app.mombaby.models.Data
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PasswordWebService {

    @FormUrlEncoded
    @POST("api/v1/user/add_password")
    suspend fun addPassword(
        @Field("password") password: String
    ): Response<Data?>

    @FormUrlEncoded
    @POST("api/v1/user/check_password")
    suspend fun checkPassword(
        @Field("password") password: String
    ): Response<Data?>

    @FormUrlEncoded
    @POST("api/v1/user/forget_password")
    suspend fun forgetPassword(
        @Field("mobile") mobile: String
    ): Response<Data?>

    @FormUrlEncoded
    @POST("api/v1/user/forget_password_check")
    suspend fun forgetPasswordCheck(
        @Field("code") code: String
    ): Response<Data?>

    @FormUrlEncoded
    @POST("api/v1/user/update_password_check")
    suspend fun updatePasswordCheck(
        @Field("status_now") statusNow: Int
    ): Response<Data?>

}