package com.app.mombaby.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.mombaby.models.pregnancyHome.PregnancyBlog

@Dao
interface PregnancyBlogsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPregnancyGuide(pregnancyBlogs: List<PregnancyBlog>)

    @Query("SELECT * FROM PregnancyBlog WHERE WEEK LIKE  :week AND CATEGORYID LIKE :catId")
    suspend fun findPregnancyGuideByWeek(week: Int, catId: Int): List<PregnancyBlog>

    @Query("SELECT * FROM PregnancyBlog")
    suspend fun get(): List<PregnancyBlog>

    @Query("SELECT * FROM PregnancyBlog WHERE type = :type")
    suspend fun getBlogsByType(type: Int): List<PregnancyBlog>

}