package com.app.mombaby.services

import com.app.mombaby.models.intro.IntroSetting
import retrofit2.http.GET

interface IntroWebService {

    @GET("api/v1/pre_login_info")
    suspend fun introInfo(): IntroSetting
    
}