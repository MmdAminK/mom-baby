package com.app.mombaby.ui.fragments.loginPages

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.mombaby.R
import com.app.mombaby.databinding.PregnancyStatusBinding
import com.app.mombaby.utils.models.CacheKeys.status
import com.app.mombaby.utils.models.CacheKeys.userFirstName
import com.app.mombaby.utils.models.DataState
import com.app.mombaby.utils.utilities.ViewUtils.showInternetWarningToast
import com.app.mombaby.viewModels.PregnancyStatusEvent
import com.app.mombaby.viewModels.PregnancyStatusViewModel
import com.app.mombaby.views.AppProgressBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PregnancyStatus : Fragment() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var binding: PregnancyStatusBinding

    val viewModel: PregnancyStatusViewModel by viewModels()

    @Inject
    lateinit var appProgressBar: AppProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.pregnancy_status, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.hello.text =
            "سلام " + sharedPreferences.getString(userFirstName, "").toString()

        binding.pregnancyStatusNotPregnantLinear.setOnClickListener {
            binding.stickySwitch.isOn = false
        }
        binding.pregnancyStatusPregnantLinear.setOnClickListener {
            binding.stickySwitch.isOn = true
        }
        binding.pregnancyStatusSubimt.setOnClickListener {
            val stickValue =
                if (binding.stickySwitch.isOn) Status.Pregnancy else Status.Menstruation
            viewModel.setStateEvent(PregnancyStatusEvent.UpdateStatus(stickValue.value))

            viewModel.dataState.observe(viewLifecycleOwner, {
                when (it) {
                    is DataState.Error -> {
                        binding.root.showInternetWarningToast()
                        appProgressBar.hide()
                    }
                    DataState.Loading -> {
                        appProgressBar.show()
                    }
                    is DataState.RetrofitError -> {
                        binding.root.showInternetWarningToast()
                        appProgressBar.hide()
                    }
                    is DataState.Success -> {
                        appProgressBar.hide()
                        sharedPreferences.edit().putInt(status, stickValue.value)
                            .apply()
                        findNavController().navigate(R.id.action_pregnancyStatus_to_appMainPage)
                    }
                }
            })
        }
    }

    enum class Status(val value: Int) {
        Menstruation(1),
        Pregnancy(2)
    }
}