package com.app.mombaby.ui.fragments.mainPage.user.password

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.mombaby.R
import com.app.mombaby.databinding.ProfilePasswordBinding
import com.app.mombaby.utils.models.CacheKeys
import com.app.mombaby.utils.models.DataState
import com.app.mombaby.utils.utilities.CommonUtils.backToPreviousDestination
import com.app.mombaby.viewModels.PasswordEvent
import com.app.mombaby.viewModels.PasswordViewModel
import com.app.mombaby.views.AppProgressBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ProfilePassword : Fragment() {

    lateinit var binding: ProfilePasswordBinding
    private val viewModel: PasswordViewModel by viewModels()


    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var appProgressBar: AppProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().backToPreviousDestination(findNavController())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.profile_password, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profilePasswordActionBar.onBackClickListener {
            findNavController().popBackStack()
        }

        binding.profilePasswordSwitch.isOn =
            sharedPreferences.getBoolean(CacheKeys.passwordEnabled, false)

        binding.profilePasswordSwitch.setOnToggledListener { _, isOn ->
            viewModel.setStateEvent(PasswordEvent.UpdatePasswordCheck(if (isOn) 0 else 1))
            viewModel.updateCheckPasswordDataState.observe(viewLifecycleOwner, {
                when (it) {
                    is DataState.Error -> {
                        appProgressBar.hide()
                    }
                    DataState.Loading -> {
                        appProgressBar.show()
                    }
                    is DataState.RetrofitError -> {
                        appProgressBar.hide()
                    }
                    is DataState.Success -> {
                        appProgressBar.hide()
                        sharedPreferences.edit().putBoolean(CacheKeys.passwordEnabled, isOn).apply()
                    }
                }
            })
        }

        binding.profilePasswordChangePassword.setOnClickListener {
            findNavController().navigate(R.id.action_profilePassword_to_profileChangePassword)
        }
    }
}