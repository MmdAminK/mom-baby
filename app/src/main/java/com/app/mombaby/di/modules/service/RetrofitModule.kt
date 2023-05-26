package com.app.mombaby.di.modules.service

import android.content.SharedPreferences
import com.app.mombaby.di.annotations.NamedAnnotation
import com.app.mombaby.utils.models.AppConstant
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
    }

    @Provides
    @Singleton
    fun provideOkHttpBuilder(): OkHttpClient.Builder {
        val httpClient = OkHttpClient.Builder()
        httpClient.followRedirects(true)
            .followSslRedirects(false)
            .readTimeout(40, TimeUnit.SECONDS)
            .connectTimeout(40, TimeUnit.SECONDS)
            .hostnameVerifier { _, _ -> true }
        return httpClient
    }

    @Provides
    @Singleton
    fun provideOkHttp(
        httpClient: OkHttpClient.Builder,
        sharedPreferences: SharedPreferences
    ): OkHttpClient {
        httpClient.addInterceptor { chain ->
            val request = chain.request().newBuilder().addHeader(
                "Auth", "Bearer ${sharedPreferences.getString("auth", "")}"
            ).build()
            chain.proceed(request)
        }
        return httpClient.build()
    }

    @Provides
    @Singleton
    fun provideRetrofitBuilder(gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(AppConstant.baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Provides
    @Singleton
    @NamedAnnotation.NotAuthenticatedRetrofit
    fun provideRetrofit(
        httpClientBuilder: OkHttpClient.Builder,
        retrofitBuilder: Retrofit.Builder
    ): Retrofit {
        return retrofitBuilder.client(httpClientBuilder.build()).build()
    }

    @Provides
    @Singleton
    fun provideAuthenticatedRetrofit(
        retrofitBuilder: Retrofit.Builder, httpClient: OkHttpClient
    ): Retrofit {
        return retrofitBuilder.client(httpClient).build()
    }
}
