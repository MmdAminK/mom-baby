package com.app.mombaby.services

import com.app.mombaby.models.Data
import com.app.mombaby.models.signup.UserReqBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpWebService {

    @POST("api/v1/user/user_update")
    suspend fun updateUser(
        @Body userReqBody: UserReqBody
    ): Response<Data>
}