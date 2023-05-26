package com.app.mombaby.di.modules.service

import com.app.mombaby.di.annotations.NamedAnnotation
import com.app.mombaby.services.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ServiceModule {

    @Singleton
    @Provides
    fun introInfo(@NamedAnnotation.NotAuthenticatedRetrofit retrofit: Retrofit): IntroWebService {
        return retrofit.create(IntroWebService::class.java)
    }

    @Singleton
    @Provides
    fun login(@NamedAnnotation.NotAuthenticatedRetrofit retrofit: Retrofit): LoginWebService {
        return retrofit.create(LoginWebService::class.java)
    }

    @Singleton
    @Provides
    fun verification(@NamedAnnotation.NotAuthenticatedRetrofit retrofit: Retrofit): VerifyWebService {
        return retrofit.create(VerifyWebService::class.java)
    }

    @Singleton
    @Provides
    fun userAuthenticationWebService(retrofit: Retrofit): UserAuthenticationWebService {
        return retrofit.create(UserAuthenticationWebService::class.java)
    }

    @Singleton
    @Provides
    fun signUp(retrofit: Retrofit): SignUpWebService {
        return retrofit.create(SignUpWebService::class.java)
    }

    @Singleton
    @Provides
    fun reminder(retrofit: Retrofit): ReminderWebService {
        return retrofit.create(ReminderWebService::class.java)
    }

    @Singleton
    @Provides
    fun editProfile(retrofit: Retrofit): EditProfileWebService {
        return retrofit.create(EditProfileWebService::class.java)
    }

    @Singleton
    @Provides
    fun appInfo(@NamedAnnotation.NotAuthenticatedRetrofit retrofit: Retrofit): AppInformationWebService {
        return retrofit.create(AppInformationWebService::class.java)
    }

    @Singleton
    @Provides
    fun sendMessage(retrofit: Retrofit): SendMessageWebService {
        return retrofit.create(SendMessageWebService::class.java)
    }

    @Singleton
    @Provides
    fun pregnancyHome(retrofit: Retrofit): PregnancyHomeWebService {
        return retrofit.create(PregnancyHomeWebService::class.java)
    }

    @Singleton
    @Provides
    fun userInfo(retrofit: Retrofit): UserWebService {
        return retrofit.create(UserWebService::class.java)
    }

    @Singleton
    @Provides
    fun password(retrofit: Retrofit): PasswordWebService {
        return retrofit.create(PasswordWebService::class.java)
    }

}