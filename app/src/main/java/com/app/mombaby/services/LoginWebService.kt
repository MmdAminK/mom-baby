package com.app.mombaby.services

import com.app.mombaby.models.login.LoginDto
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginWebService {

    @FormUrlEncoded
    @POST("api/v1/user/login")
    suspend fun login(@Field("mobile") mobile: String): LoginDto

}