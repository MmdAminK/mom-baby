package com.app.mombaby.viewModels

import androidx.lifecycle.*
import com.app.mombaby.models.Data
import com.app.mombaby.models.pregnancyHome.PregnancyHomeInfo
import com.app.mombaby.repositories.MenstruationHomeRepository
import com.app.mombaby.utils.models.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PregnancyHomeViewModel @Inject
constructor(
    private val savedState: SavedStateHandle,
    private val homeRepository: MenstruationHomeRepository,
) : ViewModel() {

    private var _dataState: MutableLiveData<DataState<PregnancyHomeInfo?>> = MutableLiveData()
    var dataState: LiveData<DataState<PregnancyHomeInfo?>> = _dataState

    private var _dataStateUpdateInfo: MutableLiveData<DataState<Data?>> = MutableLiveData()
    var dataStateUpdateInfo: LiveData<DataState<Data?>> = _dataStateUpdateInfo

    fun setStateEvent(pregnancyHomeEvent: PregnancyHomeEvent) {
        viewModelScope.launch {
            when (pregnancyHomeEvent) {
                is PregnancyHomeEvent.PregnancyInfo -> {
                    homeRepository.pregnancyHomeInfo().onEach {
                        _dataState.value = it
                    }
                        .launchIn(viewModelScope)
                }
                is PregnancyHomeEvent.UpdatePregnancyInfo -> {
                    homeRepository.updatePregnancyHomeInfo(
                        pregnancyHomeEvent.lastPeriod,
                        pregnancyHomeEvent.babyName
                    ).onEach {
                        _dataStateUpdateInfo.value = it
                    }
                        .launchIn(viewModelScope)
                }
            }
        }
    }


}

sealed class PregnancyHomeEvent {
    object PregnancyInfo : PregnancyHomeEvent()
    class UpdatePregnancyInfo(val lastPeriod: String, val babyName: String) : PregnancyHomeEvent()
}