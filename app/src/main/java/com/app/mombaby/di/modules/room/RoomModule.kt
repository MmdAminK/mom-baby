package com.app.mombaby.di.modules.room

import android.content.Context
import androidx.room.Room
import com.app.mombaby.database.MomBabyDb
import com.app.mombaby.database.PregnancyBlogsDao
import com.app.mombaby.database.PregnancyCategoryDao
import com.app.mombaby.database.user.UserDao
import com.app.mombaby.utils.models.AppConstant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {

    @Singleton
    @Provides
    fun provideAppDataBase(@ApplicationContext context: Context): MomBabyDb {
        return Room.databaseBuilder(
            context,
            MomBabyDb::class.java,
            AppConstant.appDataBaseName
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideUserDao(momBabyDb: MomBabyDb): UserDao {
        return momBabyDb.userDao()
    }

    @Singleton
    @Provides
    fun providePregnancyBlogsDao(momBabyDb: MomBabyDb): PregnancyBlogsDao {
        return momBabyDb.pregnancyBlogsDao()
    }

    @Singleton
    @Provides
    fun providePregnancyCategoryDao(momBabyDb: MomBabyDb): PregnancyCategoryDao {
        return momBabyDb.pregnancyCategoryDao()
    }
}