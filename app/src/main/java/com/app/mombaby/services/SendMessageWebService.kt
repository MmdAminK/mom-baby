package com.app.mombaby.services

import com.app.mombaby.models.Data
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface SendMessageWebService {

    @FormUrlEncoded
    @POST("api/v1/insert_comment")
    suspend fun sendMessage(
        @Field("fullname") name: String,
        @Field("mobile") mobile: String,
        @Field("comment") comment: String,
    ): Response<Data?>
}