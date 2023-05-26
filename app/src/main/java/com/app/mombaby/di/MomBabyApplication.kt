package com.app.mombaby.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import net.danlew.android.joda.JodaTimeAndroid

@HiltAndroidApp
class MomBabyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this);
    }
}