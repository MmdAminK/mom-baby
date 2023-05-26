package com.app.mombaby.repositories

import com.app.mombaby.models.intro.IntroData
import com.app.mombaby.models.intro.IntroSetting
import com.app.mombaby.services.IntroWebService
import com.app.mombaby.utils.models.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class IntroRepository
@Inject
constructor(
    private val introWebService: IntroWebService
) {
    suspend fun introInfo(): Flow<DataState<IntroSetting>> = flow {
        emit(DataState.Loading)
        try {
            val introSetting = introWebService.introInfo()
            IntroData.intro = introSetting.intro!!
            emit(DataState.Success(introSetting))
        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }

    }
}
