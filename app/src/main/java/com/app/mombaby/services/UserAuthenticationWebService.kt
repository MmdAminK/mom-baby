package com.app.mombaby.services

import com.app.mombaby.models.Data
import retrofit2.http.GET

interface UserAuthenticationWebService {

    @GET("api/v1/check_token")
    suspend fun checkAuthentication(): Data

}