package com.app.mombaby.repositories

import android.util.Log
import com.app.mombaby.database.PregnancyBlogsDao
import com.app.mombaby.models.Data
import com.app.mombaby.models.pregnancyHome.PeriodOvulation
import com.app.mombaby.models.pregnancyHome.PregnancyBlogsType
import com.app.mombaby.models.pregnancyHome.PregnancyHomeInfo
import com.app.mombaby.models.pregnancyHome.menstruation.MenstruationDto
import com.app.mombaby.services.PregnancyHomeWebService
import com.app.mombaby.utils.models.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MenstruationHomeRepository @Inject constructor(
    private val pregnancyHomeWebService: PregnancyHomeWebService,
    private val pregnancyBlogDao: PregnancyBlogsDao
) {

    @ExperimentalCoroutinesApi
    suspend fun menstruationHomeInfo(): Flow<DataState<MenstruationDto?>> = flow {
        emit(DataState.Loading)
        try {
            val response = pregnancyHomeWebService.menstruationHomeInfo()
            if (response.isSuccessful) {
                val articles = response.body()!!.articles
                Log.i("TAG", "menstruationHomeInfo: $articles")
                articles.map { it.type = PregnancyBlogsType.MAINARTICLE.ordinal }
                pregnancyBlogDao.insertPregnancyGuide(articles)
                emit(DataState.Success(response.body()))
            } else {
                emit(DataState.RetrofitError(response.errorBody()?.string()))
                emit(
                    DataState.Success(
                        MenstruationDto(
                            articles = pregnancyBlogDao.getBlogsByType(PregnancyBlogsType.MAINARTICLE.ordinal)
                        )
                    )
                )
            }
        } catch (exception: Exception) {
            emit(DataState.Error(exception))
            emit(
                DataState.Success(
                    MenstruationDto(
                        articles = pregnancyBlogDao.getBlogsByType(PregnancyBlogsType.MAINARTICLE.ordinal)
                    )
                )
            )
        }
    }

    suspend fun pregnancyHomeInfo(): Flow<DataState<PregnancyHomeInfo?>> = flow {
        emit(DataState.Loading)
        try {
            val response = pregnancyHomeWebService.pregnancyHomeInfo()
            if (response.isSuccessful) {
                emit(DataState.Success(response.body()))
            } else {
                emit(DataState.RetrofitError(response.errorBody()?.string()))

            }
        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }
    }

    suspend fun periodOvulation(): Flow<DataState<PeriodOvulation?>> = channelFlow {
        send(DataState.Loading)
        try {
            val response = pregnancyHomeWebService.periodOvulationDates()
            if (response.isSuccessful) {
                send(DataState.Success(response.body()))
            } else {
                send(DataState.RetrofitError(response.errorBody()?.string()))

            }
        } catch (exception: Exception) {
            send(DataState.Error(exception))
        }
    }

    suspend fun updatePregnancyHomeInfo(
        lastPeriod: String,
        babyName: String
    ): Flow<DataState<Data?>> = flow {
        emit(DataState.Loading)
        try {
            val response = pregnancyHomeWebService.updatePregnancyHomeInfo(lastPeriod, babyName)
            if (response.isSuccessful) {
                emit(DataState.Success(response.body()))
            } else {
                emit(DataState.RetrofitError(response.errorBody()?.string()))

            }
        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }
    }


}