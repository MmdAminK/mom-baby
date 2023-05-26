package com.app.mombaby.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.mombaby.database.user.UserDao
import com.app.mombaby.models.pregnancyHome.PregnancyBlog
import com.app.mombaby.models.pregnancyHome.categories.PregnancyCategory
import com.app.mombaby.models.user.UserEntity
import com.app.mombaby.utils.utilities.Converter

@Database(
    entities = [
        UserEntity::class,
        PregnancyBlog::class,
        PregnancyCategory::class
    ], version = 6, exportSchema = false
)
@TypeConverters(Converter::class)
abstract class MomBabyDb : RoomDatabase() {
    //Dao
    abstract fun userDao(): UserDao
    abstract fun pregnancyBlogsDao(): PregnancyBlogsDao
    abstract fun pregnancyCategoryDao(): PregnancyCategoryDao
}