package com.app.mombaby.repositories

import android.util.Log
import com.app.mombaby.database.PregnancyBlogsDao
import com.app.mombaby.database.PregnancyCategoryDao
import com.app.mombaby.models.pregnancyHome.*
import com.app.mombaby.models.pregnancyHome.categories.PregnancyArticleCategoryDto
import com.app.mombaby.models.pregnancyHome.categories.PregnancyGuideCategoryDto
import com.app.mombaby.services.PregnancyHomeWebService
import com.app.mombaby.utils.models.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PregnancyBlogsRepository @Inject constructor(
    private val pregnancyHomeWebService: PregnancyHomeWebService,
    private val pregnancyBlogDao: PregnancyBlogsDao,
    private val pregnancyCategoryDao: PregnancyCategoryDao
) {

    suspend fun pregnancyGuideList(category: Int, week: Int): Flow<DataState<PregnancyGuideDto?>> =
        flow {
            emit(DataState.Loading)
            try {
                val response = pregnancyHomeWebService.guideList(category, week)

                if (response.isSuccessful) {
                    val guides = response.body()!!.guides
                    guides.map { it.type = PregnancyBlogsType.GUIDE.ordinal }
                    pregnancyBlogDao.insertPregnancyGuide(guides)
                } else
                    emit(DataState.RetrofitError(response.errorBody()?.string()))
                emit(
                    DataState.Success(
                        PregnancyGuideDto(
                            pregnancyBlogDao.getBlogsByType(PregnancyBlogsType.GUIDE.ordinal)
                                .filter { it.week == week })
                    )
                )

            } catch (exception: Exception) {
                emit(
                    DataState.Success(
                        PregnancyGuideDto(pregnancyBlogDao.getBlogsByType(PregnancyBlogsType.GUIDE.ordinal))
                    )
                )
                emit(DataState.Error(exception))
            }
        }

    suspend fun pregnancyGuideCategoryList(week: Int): Flow<DataState<PregnancyGuideCategoryDto?>> =
        flow {
            emit(DataState.Loading)
            try {
                val response = pregnancyHomeWebService.guideCategoryList(week)
                if (response.isSuccessful) {
                    val categories = response.body()!!.guidesCategory
                    categories.map { it.type = PregnancyBlogsType.GUIDE.ordinal }
                    pregnancyCategoryDao.insertPregnancyCategory(categories)
                } else
                    emit(DataState.RetrofitError(response.errorBody()?.string()))
                emit(
                    DataState.Success(
                        PregnancyGuideCategoryDto(
                            pregnancyCategoryDao.getCategoryByType(PregnancyBlogsType.GUIDE.ordinal)
                        )
                    )
                )

            } catch (exception: Exception) {
                emit(
                    DataState.Success(
                        PregnancyGuideCategoryDto(
                            pregnancyCategoryDao.getCategoryByType(PregnancyBlogsType.GUIDE.ordinal)
                        )
                    )
                )
                emit(DataState.Error(exception))
            }
        }

    suspend fun pregnancyArticleList(category: Int): Flow<DataState<PregnancyArticleDto?>> =
        flow {
            emit(DataState.Loading)
            try {
                val response = pregnancyHomeWebService.articleList(category)
                if (response.isSuccessful) {
                    val articles = response.body()!!.articles
                    articles.map { it.type = PregnancyBlogsType.ARTICLE.ordinal }
                    pregnancyBlogDao.insertPregnancyGuide(articles)
                    emit(DataState.Success(response.body()))

                } else
                    emit(DataState.RetrofitError(response.errorBody()?.string()))
                Log.i(
                    "TAG", "pregnancyArticleList: ${
                        pregnancyBlogDao.getBlogsByType(PregnancyBlogsType.ARTICLE.ordinal)
                    }"
                )
                Log.i(
                    "TAG", "pregnancyArticleList: ${
                        pregnancyBlogDao.get()
                    }"
                )
//                emit(
//                    DataState.Success(
//                        PregnancyArticleDto(
//                            (pregnancyBlogDao.get() as ArrayList).filter { it.type == PregnancyBlogsType.ARTICLE.ordinal }
//                        )
//                    )
//                )
            } catch (exception: Exception) {
                emit(
                    DataState.Success(
                        PregnancyArticleDto(
                            pregnancyBlogDao.getBlogsByType(PregnancyBlogsType.ARTICLE.ordinal)
                        )
                    )
                )
                emit(DataState.Error(exception))
            }
        }

    suspend fun pregnancyArticleCategoryList(): Flow<DataState<PregnancyArticleCategoryDto?>> =
        flow {
            emit(DataState.Loading)
            try {
                val blogType = PregnancyBlogsType.ARTICLE.ordinal
                val mainBlogType = PregnancyBlogsType.MAINARTICLE.ordinal
                val response = pregnancyHomeWebService.articleCategoryList()
                if (response.isSuccessful) {
                    val categories = response.body()!!.articleCategory
                    val articles = ArrayList<PregnancyBlog>() /*response.body()!!.articles*/
                    categories.map { it.type = blogType }
//                    articles.map { it.type = mainBlogType }
                    pregnancyCategoryDao.insertPregnancyCategory(categories)
//                    pregnancyBlogDao.insertPregnancyGuide(articles)
                    emit(DataState.Success(response.body()))
                } else
                    emit(DataState.RetrofitError(response.errorBody()?.string()))
                emit(
                    DataState.Success(
                        PregnancyArticleCategoryDto(
                            pregnancyCategoryDao.getCategoryByType(blogType),
                            pregnancyBlogDao.getBlogsByType(mainBlogType)
                        )
                    )
                )
            } catch (exception: Exception) {
                emit(
                    DataState.Success(
                        PregnancyArticleCategoryDto(
                            pregnancyCategoryDao.getCategoryByType(PregnancyBlogsType.ARTICLE.ordinal),
                            pregnancyBlogDao.getBlogsByType(PregnancyBlogsType.MAINARTICLE.ordinal)
                        )
                    )
                )
                emit(DataState.Error(exception))
            }
        }

    suspend fun article(id: Int): Flow<DataState<SingleBlog?>> = flow {
        emit(DataState.Loading)
        try {
            val response = pregnancyHomeWebService.article(id)
            if (response.isSuccessful)
                emit(DataState.Success(response.body()))
            else
                emit(DataState.RetrofitError(response.errorBody()?.string()))
        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }
    }

    suspend fun guide(id: Int): Flow<DataState<SingleBlog?>> = flow {
        emit(DataState.Loading)
        try {
            val response = pregnancyHomeWebService.guide(id)
            if (response.isSuccessful)
                emit(DataState.Success(response.body()))
            else
                emit(DataState.RetrofitError(response.errorBody()?.string()))
        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }
    }

}