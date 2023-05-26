package com.app.mombaby.viewModels

import androidx.lifecycle.*
import com.app.mombaby.models.Data
import com.app.mombaby.models.signup.UserReqBody
import com.app.mombaby.repositories.SignUpRepository
import com.app.mombaby.utils.models.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class SingUpViewModel @Inject
constructor(
    private val savedState: SavedStateHandle,
    private val signUpRepository: SignUpRepository
) : ViewModel() {

    private val _dataState: MutableLiveData<DataState<Data?>> = MutableLiveData()
    var dataState: LiveData<DataState<Data?>> = _dataState

    fun setStateEvent(signUpEvent: SignUpEvent) {
        viewModelScope.launch {
            when (signUpEvent) {
                is SignUpEvent.SigningUp -> {
                    signUpRepository.updateUser(signUpEvent.userReqBody).onEach {
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

sealed class SignUpEvent {
    //Todo: make UserDto
    class SigningUp(val userReqBody: UserReqBody) : SignUpEvent()
}
