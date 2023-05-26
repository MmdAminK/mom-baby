package com.app.mombaby.viewModels

import androidx.lifecycle.*
import com.app.mombaby.models.Data
import com.app.mombaby.models.signup.UserReqBody
import com.app.mombaby.models.user.UserDtoObj
import com.app.mombaby.repositories.EditProfileRepository
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
class EditProfileViewModel @Inject constructor(
    private val savedState: SavedStateHandle,
    private val editProfileRepository: EditProfileRepository,
    private val signUpRepository: SignUpRepository,
) : ViewModel() {

    private val _dataState: MutableLiveData<DataState<UserDtoObj?>> = MutableLiveData()
    var dataState: LiveData<DataState<UserDtoObj?>> = _dataState

    private val _dataStateUpdateUser: MutableLiveData<DataState<Data?>> = MutableLiveData()
    var dataStateUpdateUser: LiveData<DataState<Data?>> = _dataStateUpdateUser

    fun setStateEvent(editProfileEvent: EditProfileEvent) {
        viewModelScope.launch {
            when (editProfileEvent) {
                is EditProfileEvent.UserInfo -> {
                    editProfileRepository.userInfo().onEach {
                        _dataState.value = it
                    }
                        .launchIn(viewModelScope)
                }

                is EditProfileEvent.UpdateUser -> {
                    signUpRepository.updateUser(editProfileEvent.userReqBody).onEach {
                        _dataStateUpdateUser.value = it
                    }
                        .launchIn(viewModelScope)
                }
            }
        }
    }

    fun clearObservers() {
        _dataState.value = null
        _dataStateUpdateUser.value = null
    }
}

sealed class EditProfileEvent {
    object UserInfo : EditProfileEvent()
    class UpdateUser(val userReqBody: UserReqBody) : EditProfileEvent()
}