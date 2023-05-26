package com.app.mombaby.viewModels

import androidx.lifecycle.*
import com.app.mombaby.models.Data
import com.app.mombaby.repositories.UserRepository
import com.app.mombaby.utils.models.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PregnancyStatusViewModel @Inject
constructor(
    private val savedState: SavedStateHandle,
    private val userRepository: UserRepository
) : ViewModel() {

    private var _dataState: MutableLiveData<DataState<Data?>> = MutableLiveData()
    var dataState: LiveData<DataState<Data?>> = _dataState

    fun setStateEvent(pregnancyStatusEvent: PregnancyStatusEvent) {
        viewModelScope.launch {
            when (pregnancyStatusEvent) {
                is PregnancyStatusEvent.UpdateStatus -> {
                    userRepository.updateUserStatus(pregnancyStatusEvent.status).onEach {
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

sealed class PregnancyStatusEvent {
    class UpdateStatus(val status: Int) : PregnancyStatusEvent()
}