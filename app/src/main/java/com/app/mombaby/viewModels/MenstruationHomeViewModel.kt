package com.app.mombaby.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.mombaby.models.Data
import com.app.mombaby.models.pregnancyHome.PeriodOvulation
import com.app.mombaby.models.pregnancyHome.menstruation.MenstruationDto
import com.app.mombaby.repositories.MenstruationHomeRepository
import com.app.mombaby.repositories.UserRepository
import com.app.mombaby.utils.models.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class MenstruationHomeViewModel @Inject
constructor(
    private val menstruationHomeRepository: MenstruationHomeRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    var _dataState: MutableLiveData<DataState<MenstruationDto?>> = MutableLiveData()
    var dataState: LiveData<DataState<MenstruationDto?>> = _dataState

    private var _dataStateUpdateUSerInfo: MutableLiveData<DataState<Data?>> = MutableLiveData()
    var dataStateUpdateUSerInfo: LiveData<DataState<Data?>> = _dataStateUpdateUSerInfo

    private var _dataStatePeriodOvulation: MutableLiveData<DataState<PeriodOvulation?>> =
        MutableLiveData()
    var dataStatePeriodOvulation: LiveData<DataState<PeriodOvulation?>> = _dataStatePeriodOvulation

    fun setStateEvent(menstruationEvent: MenstruationEvent) {
        viewModelScope.launch {
            when (menstruationEvent) {
                is MenstruationEvent.MenstruationInfo -> {
                    menstruationHomeRepository.menstruationHomeInfo().onEach {
                        _dataState.value = it
                    }
                        .launchIn(viewModelScope)
                }
                is MenstruationEvent.UpdateUSerInfo -> {
                    userRepository.sendUserInfo(
                        menstruationEvent.lastPeriod,
                        menstruationEvent.timeBetWeenPeriod,
                        menstruationEvent.periodTime
                    ).onEach {
                        _dataStateUpdateUSerInfo.value = it
                    }
                        .launchIn(viewModelScope)
                }
                MenstruationEvent.PeriodOvulation -> {
                    menstruationHomeRepository.periodOvulation().onEach {
                        _dataStatePeriodOvulation.value = it
                    }
                        .launchIn(viewModelScope)
                }
            }
        }
    }


}

sealed class MenstruationEvent {
    object MenstruationInfo : MenstruationEvent()
    object PeriodOvulation : MenstruationEvent()
    class UpdateUSerInfo(
        val lastPeriod: String, val timeBetWeenPeriod: Int, val periodTime: Int
    ) : MenstruationEvent()
}