package com.app.mombaby.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.mombaby.models.Data
import com.app.mombaby.repositories.PasswordRepository
import com.app.mombaby.utils.models.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class PasswordViewModel @Inject
constructor(
    private val passwordRepository: PasswordRepository
) : ViewModel() {

    private var _addPasswordDataState: MutableLiveData<DataState<Data?>> = MutableLiveData()
    var addPasswordDataState: LiveData<DataState<Data?>> = _addPasswordDataState

    private var _checkPasswordDataState: MutableLiveData<DataState<Data?>> = MutableLiveData()
    var checkPasswordDataState: LiveData<DataState<Data?>> = _checkPasswordDataState

    private var _forgetPasswordDataState: MutableLiveData<DataState<Data?>> = MutableLiveData()
    var forgetPasswordDataState: LiveData<DataState<Data?>> = _forgetPasswordDataState

    private var _forgetPasswordCheckDataState: MutableLiveData<DataState<Data?>> = MutableLiveData()
    var forgetPasswordCheckDataState: LiveData<DataState<Data?>> = _forgetPasswordCheckDataState

    private var _updateCheckPasswordDataState: MutableLiveData<DataState<Data?>> = MutableLiveData()
    var updateCheckPasswordDataState: LiveData<DataState<Data?>> = _updateCheckPasswordDataState

    fun setStateEvent(passwordEvent: PasswordEvent) {
        viewModelScope.launch {
            when (passwordEvent) {
                is PasswordEvent.AddPassword -> {
                    passwordRepository.addPassword(passwordEvent.password).onEach {
                        _addPasswordDataState.value = it
                    }.launchIn(viewModelScope)
                }
                is PasswordEvent.CheckPassword -> {
                    passwordRepository.checkPassword(passwordEvent.password).onEach {
                        _checkPasswordDataState.value = it
                    }.launchIn(viewModelScope)
                }
                is PasswordEvent.ForgetPassword -> {
                    passwordRepository.forgetPassword(passwordEvent.phoneNumber).onEach {
                        _forgetPasswordDataState.value = it
                    }.launchIn(viewModelScope)
                }
                is PasswordEvent.ForgetPasswordCheck -> {
                    passwordRepository.forgetPasswordCheck(passwordEvent.code).onEach {
                        _forgetPasswordCheckDataState.value = it
                    }.launchIn(viewModelScope)
                }
                is PasswordEvent.UpdatePasswordCheck -> {
                    passwordRepository.updatePasswordCheck(passwordEvent.status).onEach {
                        _updateCheckPasswordDataState.value = it
                    }.launchIn(viewModelScope)
                }
            }
        }
    }

}

sealed class PasswordEvent {
    class AddPassword(val password: String) : PasswordEvent()
    class CheckPassword(val password: String) : PasswordEvent()
    class ForgetPassword(val phoneNumber: String) : PasswordEvent()
    class ForgetPasswordCheck(val code: String) : PasswordEvent()
    class UpdatePasswordCheck(val status: Int) : PasswordEvent()
}