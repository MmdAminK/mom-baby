package com.app.mombaby.viewModels

import androidx.lifecycle.*
import com.app.mombaby.models.Data
import com.app.mombaby.models.intro.IntroSetting
import com.app.mombaby.repositories.IntroRepository
import com.app.mombaby.repositories.LoginRepository
import com.app.mombaby.utils.models.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class SplashScreenViewModel
@Inject
constructor(
    private val savedState: SavedStateHandle,
    private val introRepository: IntroRepository,
    private val loginRepository: LoginRepository
) : ViewModel() {
    private val _dataState: MutableLiveData<DataState<IntroSetting>> = MutableLiveData()
    var dataState: LiveData<DataState<IntroSetting>> = _dataState

    private val _checkAuthDataState: MutableLiveData<DataState<Data>> = MutableLiveData()
    var checkAuthDataState: LiveData<DataState<Data>> = _checkAuthDataState

    fun setStateEvent(IntroInfoEvent: IntroEvent) {
        viewModelScope.launch {
            when (IntroInfoEvent) {
                is IntroEvent.IntroInfoEvent -> {
                    introRepository.introInfo().onEach {
                        _dataState.value = it
                    }
                        .launchIn(viewModelScope)
                }
                IntroEvent.CheckAuth -> {
                    loginRepository.checkAuthentication().onEach {
                        _checkAuthDataState.value = it
                    }
                        .launchIn(viewModelScope)
                }
            }
        }
    }
}

sealed class IntroEvent {
    object IntroInfoEvent : IntroEvent()
    object CheckAuth : IntroEvent()
}