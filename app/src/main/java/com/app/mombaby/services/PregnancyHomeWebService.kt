package com.app.mombaby.services

import com.app.mombaby.models.Data
import com.app.mombaby.models.pregnancyHome.*
import com.app.mombaby.models.pregnancyHome.categories.PregnancyArticleCategoryDto
import com.app.mombaby.models.pregnancyHome.categories.PregnancyGuideCategoryDto
import com.app.mombaby.models.pregnancyHome.menstruation.MenstruationDto
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface PregnancyHomeWebService {

    @FormUrlEncoded
    @POST("api/v1/pregnancy_guide_list_by_cat")
    suspend fun guideList(
        @Field("cat_id") categoryId: Int,
        @Field("week") week: Int
    ): Response<PregnancyGuideDto?>


    @FormUrlEncoded
    @POST("api/v1/pregnancy_guide_list")
    suspend fun guideCategoryList(
        @Field("week") week: Int
    ): Response<PregnancyGuideCategoryDto?>


    @FormUrlEncoded
    @POST("api/v1/articles_list_by_cat")
    suspend fun articleList(
        @Field("cat_id") categoryId: Int
    ): Response<PregnancyArticleDto?>


    @GET("api/v1/articles_list")
    suspend fun articleCategoryList(
    ): Response<PregnancyArticleCategoryDto?>


    @POST("api/v1/period_home_info")
    suspend fun menstruationHomeInfo(
    ): Response<MenstruationDto?>


    @POST("api/v1/pregnancy_home_info")
    suspend fun pregnancyHomeInfo(
    ): Response<PregnancyHomeInfo?>


    @GET("api/v1/period_ovulation_dates")
    suspend fun periodOvulationDates(
    ): Response<PeriodOvulation?>


    @FormUrlEncoded
    @POST("api/v1/update_user_info_pregnancy")
    suspend fun updatePregnancyHomeInfo(
        @Field("last_period") lastPeriod: String,
        @Field("nini_name") babyName: String,
    ): Response<Data?>

    @FormUrlEncoded
    @POST("api/v1/article")
    suspend fun article(
        @Field("id") id: Int
    ): Response<SingleBlog>

    @FormUrlEncoded
    @POST("api/v1/pregnancy_guide")
    suspend fun guide(
        @Field("id") id: Int
    ): Response<SingleBlog>

}