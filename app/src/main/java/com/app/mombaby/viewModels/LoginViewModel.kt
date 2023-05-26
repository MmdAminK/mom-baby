package com.app.mombaby.viewModels

import androidx.lifecycle.*
import com.app.mombaby.models.login.LoginDto
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
class LoginViewModel
@Inject
constructor(
    private val savedState: SavedStateHandle,
    private val loginRepository: LoginRepository
) : ViewModel() {
    private var _dataState: MutableLiveData<DataState<LoginDto>> = MutableLiveData()
    var dataState: LiveData<DataState<LoginDto>> = _dataState

    fun setStateEvent(loginEvent: LoginEvent) {
        viewModelScope.launch {
            when (loginEvent) {
                is LoginEvent.Login -> {
                    loginRepository.login(loginEvent.mobile).onEach {
                        _dataState.value = it
                    }
                        .launchIn(viewModelScope)
                }
            }
        }
    }

    fun clearObservers() {
        _dataState.value = null
    }

}

sealed class LoginEvent {
    class Login(val mobile: String) : LoginEvent()
}