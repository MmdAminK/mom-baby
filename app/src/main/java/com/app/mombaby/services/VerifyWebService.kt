package com.app.mombaby.services

import com.app.mombaby.models.verify.VerifyDto
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface VerifyWebService {

    @FormUrlEncoded
    @POST("api/v1/user/one/time/password/validate")
    suspend fun verify(
        @Field("mobile_verification_code") authCode: String,
        @Field("mobile") mobile: String
    ): Response<VerifyDto>
}