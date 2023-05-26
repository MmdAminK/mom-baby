package com.app.mombaby.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.mombaby.models.pregnancyHome.categories.PregnancyCategory

@Dao
interface PregnancyCategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPregnancyCategory(PregnancyCategories: List<PregnancyCategory>)

    @Query("SELECT * FROM PregnancyCategory")
    suspend fun get(): List<PregnancyCategory>

    @Query("SELECT * FROM PregnancyCategory WHERE TYPE LIKE :type")
    suspend fun getCategoryByType(type: Int): List<PregnancyCategory>
}