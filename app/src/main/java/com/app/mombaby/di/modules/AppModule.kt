package com.app.mombaby.di.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.app.mombaby.R
import com.app.mombaby.utils.models.AppConstant
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideRequestOption(): RequestOptions {
        return RequestOptions().placeholder(R.drawable.no_image_available).error(R.drawable.error)
    }

    @Provides
    @Singleton
    fun provideRequestManager(
        application: Application,
        requestOptions: RequestOptions
    ): RequestManager {
        return Glide.with(application).setDefaultRequestOptions(requestOptions)
    }

    @Singleton
    @Provides
    fun provideContext(app: Application): Context? {
        return app.applicationContext
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context?): SharedPreferences {
        return context!!.getSharedPreferences(
            "${AppConstant.appName}Preferences",
            Context.MODE_PRIVATE
        )
    }
}