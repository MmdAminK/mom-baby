package com.app.mombaby.di.modules

import android.content.Context
import com.app.mombaby.views.AppProgressBar
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @Provides
    @ActivityScoped
    fun provideAppProgressBar(@ActivityContext context: Context): AppProgressBar {
        return AppProgressBar.instance(context)
    }
}