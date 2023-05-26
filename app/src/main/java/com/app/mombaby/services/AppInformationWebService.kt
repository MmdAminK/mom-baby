package com.app.mombaby.services

import com.app.mombaby.models.appInfo.AboutUsDto
import com.app.mombaby.models.appInfo.ContactUsDto
import com.app.mombaby.models.appInfo.FriendsDto
import com.app.mombaby.models.appInfo.RulesDto
import retrofit2.Response
import retrofit2.http.GET

interface AppInformationWebService {

    @GET("api/v1/about")
    suspend fun aboutUs(): Response<AboutUsDto>

    @GET("api/v1/contact")
    suspend fun contactUs(): Response<ContactUsDto>

    @GET("api/v1/rules")
    suspend fun rules(): Response<RulesDto>

    @GET("api/v1/apps")
    suspend fun appLinks(): Response<FriendsDto>

}