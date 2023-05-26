package com.app.mombaby.viewModels

import android.util.Log
import androidx.lifecycle.*
import com.app.mombaby.models.verify.VerifyDto
import com.app.mombaby.repositories.VerifyRepository
import com.app.mombaby.utils.models.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class VerifyViewModel
@Inject
constructor(
    private val savedState: SavedStateHandle,
    private val verifyRepository: VerifyRepository
) : ViewModel() {

    private val _dataState: MutableLiveData<DataState<VerifyDto?>> = MutableLiveData()
    var dataState: LiveData<DataState<VerifyDto?>> = _dataState

    fun setStateEvent(verifyEvent: VerifyEvent) {
        Log.i("TAG", "setStateEvent: hii")
        viewModelScope.launch {
            when (verifyEvent) {
                is VerifyEvent.Verify -> {
                    verifyRepository.verify(verifyEvent.authCode, verifyEvent.mobile).onEach {
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

sealed class VerifyEvent {
    class Verify(val authCode: String, val mobile: String) : VerifyEvent()
}
