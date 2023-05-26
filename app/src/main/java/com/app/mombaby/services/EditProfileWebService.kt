package com.app.mombaby.services

import com.app.mombaby.models.user.UserDtoObj
import retrofit2.Response
import retrofit2.http.GET

interface EditProfileWebService {

    @GET("api/v1/user/get_user")
    suspend fun userInfo(): Response<UserDtoObj>

}