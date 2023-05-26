package com.app.mombaby.viewModels

import androidx.lifecycle.*
import com.app.mombaby.models.Data
import com.app.mombaby.models.reminder.ReminderData
import com.app.mombaby.models.reminder.ReminderDto
import com.app.mombaby.models.reminder.ReminderReqBody
import com.app.mombaby.repositories.ReminderRepository
import com.app.mombaby.utils.models.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ReminderViewModel @Inject
constructor(
    private val savedState: SavedStateHandle,
    private val reminderRepository: ReminderRepository
) : ViewModel() {
    var _dataState: MutableLiveData<DataState<ReminderDto?>> = MutableLiveData()
    var dataState: LiveData<DataState<ReminderDto?>> = _dataState

    private var _dataStateAddReminder: MutableLiveData<DataState<ReminderData?>> = MutableLiveData()
    var dataStateAddReminder: LiveData<DataState<ReminderData?>> = _dataStateAddReminder

    private var _dataStateRemoveReminder: MutableLiveData<DataState<Data?>> = MutableLiveData()
    var dataStateRemoveReminder: LiveData<DataState<Data?>> = _dataStateRemoveReminder

    private var _dataStateEditReminder: MutableLiveData<DataState<Data?>> = MutableLiveData()
    var dataStateEditReminder: LiveData<DataState<Data?>> = _dataStateEditReminder

    fun setStateEvent(reminderEvent: ReminderEvent) {
        viewModelScope.launch {
            when (reminderEvent) {
                is ReminderEvent.Reminders -> {
                    reminderRepository.reminders().onEach {
                        _dataState.value = it
                    }
                        .launchIn(viewModelScope)
                }
                is ReminderEvent.AddReminder -> {
                    reminderRepository.addReminder(reminderEvent.reminder).onEach {
                        _dataStateAddReminder.value = it
                    }
                        .launchIn(viewModelScope)
                }
                is ReminderEvent.RemoveReminder -> {
                    reminderRepository.removeReminder(reminderEvent.reminderId).onEach {
                        _dataStateRemoveReminder.value = it
                    }
                        .launchIn(viewModelScope)
                }
                is ReminderEvent.EditReminder -> {
                    reminderRepository.editReminder(reminderEvent.reminder).onEach {
                        _dataStateEditReminder.value = it
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

sealed class ReminderEvent {
    object Reminders : ReminderEvent()
    class AddReminder(val reminder: ReminderReqBody) : ReminderEvent()
    class RemoveReminder(val reminderId: Int) : ReminderEvent()
    class EditReminder(val reminder: ReminderReqBody) : ReminderEvent()
}