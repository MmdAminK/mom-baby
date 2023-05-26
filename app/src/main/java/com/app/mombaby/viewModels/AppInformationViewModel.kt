package com.app.mombaby.viewModels

import androidx.lifecycle.*
import com.app.mombaby.models.Data
import com.app.mombaby.models.appInfo.AboutUsDto
import com.app.mombaby.models.appInfo.ContactUsDto
import com.app.mombaby.models.appInfo.FriendsDto
import com.app.mombaby.models.appInfo.RulesDto
import com.app.mombaby.repositories.AppInfoRepository
import com.app.mombaby.utils.models.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppInformationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val appInfoRepository: AppInfoRepository
) : ViewModel() {


    private var _dataStateAboutUs: MutableLiveData<DataState<AboutUsDto?>> = MutableLiveData()
    var dataStateAboutUs: LiveData<DataState<AboutUsDto?>> = _dataStateAboutUs

    private var _dataStateContactUs: MutableLiveData<DataState<ContactUsDto?>> = MutableLiveData()
    var dataStateContactUs: LiveData<DataState<ContactUsDto?>> = _dataStateContactUs

    private var _dataStateRules: MutableLiveData<DataState<RulesDto?>> = MutableLiveData()
    var dataStateRules: LiveData<DataState<RulesDto?>> = _dataStateRules

    private var _dataStateFriends: MutableLiveData<DataState<FriendsDto?>> = MutableLiveData()
    var dataStateFriends: LiveData<DataState<FriendsDto?>> = _dataStateFriends

    private var _dataStateSendMessage: MutableLiveData<DataState<Data?>> = MutableLiveData()
    var dataStateSendMessage: LiveData<DataState<Data?>> = _dataStateSendMessage

    fun setStateEvent(appInfoEvent: AppInfoEvent) {
        viewModelScope.launch {
            when (appInfoEvent) {
                is AppInfoEvent.AboutUs -> {
                    appInfoRepository.aboutUs().onEach {
                        _dataStateAboutUs.value = it
                    }
                        .launchIn(viewModelScope)
                }
                is AppInfoEvent.ContactUs -> {
                    appInfoRepository.contactUs().onEach {
                        _dataStateContactUs.value = it
                    }
                        .launchIn(viewModelScope)
                }
                is AppInfoEvent.Rules -> {
                    appInfoRepository.rules().onEach {
                        _dataStateRules.value = it
                    }
                        .launchIn(viewModelScope)
                }
                AppInfoEvent.Friends -> {
                    appInfoRepository.friends().onEach {
                        _dataStateFriends.value = it
                    }
                        .launchIn(viewModelScope)
                }
                is AppInfoEvent.SendMessage -> {
                    appInfoRepository.sendMessage(
                        appInfoEvent.name,
                        appInfoEvent.phoneNumber,
                        appInfoEvent.urMessage
                    ).onEach {
                        _dataStateSendMessage.value = it
                    }
                        .launchIn(viewModelScope)
                }
            }
        }
    }
}

sealed class AppInfoEvent {
    object AboutUs : AppInfoEvent()
    object ContactUs : AppInfoEvent()
    class SendMessage(val name: String, val phoneNumber: String, val urMessage: String) :
        AppInfoEvent()

    object Rules : AppInfoEvent()
    object Friends : AppInfoEvent()
}