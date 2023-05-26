package com.app.mombaby.di.modules.repository

import android.content.SharedPreferences
import com.app.mombaby.database.PregnancyBlogsDao
import com.app.mombaby.database.PregnancyCategoryDao
import com.app.mombaby.repositories.*
import com.app.mombaby.services.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun introRepository(introWebService: IntroWebService): IntroRepository {
        return IntroRepository(introWebService)
    }

    @Singleton
    @Provides
    fun loginRepository(
        loginWebService: LoginWebService,
        userAuthenticationWebService: UserAuthenticationWebService
    ): LoginRepository {
        return LoginRepository(loginWebService, userAuthenticationWebService)
    }

    @Singleton
    @Provides
    fun verifyRepository(verifyWebService: VerifyWebService): VerifyRepository {
        return VerifyRepository(verifyWebService)
    }

    @Singleton
    @Provides
    fun signUpRepository(signUpWebService: SignUpWebService): SignUpRepository {
        return SignUpRepository(signUpWebService)
    }


    @Singleton
    @Provides
    fun reminderRepository(reminderWebService: ReminderWebService): ReminderRepository {
        return ReminderRepository(reminderWebService)
    }

    @Singleton
    @Provides
    fun editProfileRepository(editProfileWebService: EditProfileWebService): EditProfileRepository {
        return EditProfileRepository(editProfileWebService)
    }

    @Singleton
    @Provides
    fun aboutUsRepository(
        appInformationWebService: AppInformationWebService,
        sendMessageWebService: SendMessageWebService,
        sharedPreferences: SharedPreferences
    ): AppInfoRepository {
        return AppInfoRepository(appInformationWebService, sendMessageWebService, sharedPreferences)
    }

    @Singleton
    @Provides
    fun pregnancyHomeRepository(
        pregnancyHomeWebService: PregnancyHomeWebService,
        pregnancyBlogDao: PregnancyBlogsDao,
        pregnancyCategoryDao: PregnancyCategoryDao
    ): PregnancyBlogsRepository {
        return PregnancyBlogsRepository(
            pregnancyHomeWebService,
            pregnancyBlogDao,
            pregnancyCategoryDao
        )
    }

    @ExperimentalCoroutinesApi
    @Singleton
    @Provides
    fun menstruationHomeRepository(
        pregnancyHomeWebService: PregnancyHomeWebService,
        pregnancyBlogDao: PregnancyBlogsDao
    ): MenstruationHomeRepository {
        return MenstruationHomeRepository(pregnancyHomeWebService, pregnancyBlogDao)
    }

    @Singleton
    @Provides
    fun userRepository(
        userWebService: UserWebService,
    ): UserRepository {
        return UserRepository(userWebService)
    }

    @Singleton
    @Provides
    fun passwordRepository(
        passwordWebService: PasswordWebService
    ): PasswordRepository {
        return PasswordRepository(passwordWebService)
    }
}