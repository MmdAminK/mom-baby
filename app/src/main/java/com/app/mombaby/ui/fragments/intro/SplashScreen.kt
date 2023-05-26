package com.app.mombaby.ui.fragments.intro

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.mombaby.R
import com.app.mombaby.databinding.SplashScreenBinding
import com.app.mombaby.utils.animations.PulseAnimation
import com.app.mombaby.utils.models.CacheKeys
import com.app.mombaby.utils.models.DataState
import com.app.mombaby.utils.utilities.PreferencesUtils.isStringValueEmpty
import com.app.mombaby.utils.utilities.ViewUtils.showInternetWarningToast
import com.app.mombaby.viewModels.IntroEvent
import com.app.mombaby.viewModels.SplashScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SplashScreen : Fragment() {

    private val viewModel: SplashScreenViewModel by viewModels()
    private lateinit var binding: SplashScreenBinding
    private val splashScreenDelay: Long = 3_000

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.splash_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PulseAnimation(binding.splashImageView).start()

        viewModel.setStateEvent(IntroEvent.IntroInfoEvent)
        CoroutineScope(Main).launch {
            delay(splashScreenDelay)
            getIntroData()
        }
    }

    private fun getIntroData() {
        viewModel.dataState.observe(viewLifecycleOwner, {
            when (it) {
                is DataState.Loading -> {
                }
                is DataState.Success -> {
                    var bundle: Bundle? = null
                    val destinationId =
                        if (sharedPreferences.isStringValueEmpty(CacheKeys.auth))
                            if (sharedPreferences.getBoolean(CacheKeys.introSeen, false))
                                R.id.action_splashScreen_to_login
                            else {
                                bundle = bundleOf("introData" to it.data.intro)
                                R.id.action_splashScreen_to_intro
                            }
                        else {
                            if (sharedPreferences.getBoolean(CacheKeys.passwordEnabled, false))
                                R.id.action_splashScreen_to_passwordLogin
                            else
                                R.id.action_splashScreen_to_appMainPage
                        }
                    if (sharedPreferences.getString(CacheKeys.auth, "").isNullOrEmpty()) {
                        findNavController().navigate(destinationId, bundle)
                    } else {

                        viewModel.setStateEvent(IntroEvent.CheckAuth)
                        viewModel.checkAuthDataState.observe(viewLifecycleOwner, { dataState ->
                            when (dataState) {
                                is DataState.Error -> {
                                    if (dataState.exception.toString().split(" ")
                                            .joinToString("") == "retrofit2.HttpException:HTTP401"
                                    ) {
                                        findNavController().navigate(
                                            R.id.action_splashScreen_to_login, bundle
                                        )
                                        binding.root.showInternetWarningToast("دستگاه دیگری با این شماره وارد شده است")
                                    } else
                                        binding.root.showInternetWarningToast()
                                }
                                DataState.Loading -> {
                                }
                                is DataState.RetrofitError -> {
                                    binding.root.showInternetWarningToast()
                                }
                                is DataState.Success -> {
                                    findNavController().navigate(destinationId, bundle)
                                }
                            }
                        })
                    }

                }
                is DataState.Error -> {
                    binding.root.showInternetWarningToast()
                }
                is DataState.RetrofitError -> {
                    binding.root.showInternetWarningToast()
                }
            }
        })
    }
}