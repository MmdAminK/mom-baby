package com.app.mombaby.ui.fragments.mainPage.user

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
import com.app.mombaby.databinding.EditProfileBinding
import com.app.mombaby.models.signup.UserReqBody
import com.app.mombaby.utils.models.DataState
import com.app.mombaby.utils.utilities.ValidationRegexChecker
import com.app.mombaby.utils.utilities.ViewUtils.onTextChangeListener
import com.app.mombaby.utils.utilities.ViewUtils.showInternetWarningToast
import com.app.mombaby.viewModels.EditProfileEvent
import com.app.mombaby.viewModels.EditProfileViewModel
import com.app.mombaby.views.AppProgressBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@AndroidEntryPoint
class EditProfile : Fragment() {

    lateinit var binding: EditProfileBinding

    val viewModel: EditProfileViewModel by viewModels()

    @Inject
    lateinit var progressBar: AppProgressBar

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.edit_profile, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkEmailValidation()
        binding.editProfileSubmit.setOnClickListener {
            if (!binding.editProfileEmailTextField.isErrorEnabled) {
                viewModel.setStateEvent(
                    EditProfileEvent.UpdateUser(
                        UserReqBody(
                            binding.editProfileFirstNameEditText.text?.toString(),
                            binding.editProfileLastNameEditText.text?.toString(),
                            binding.editProfileEmailEditText.text?.toString(),
                            binding.editProfileAgeEditText.text?.toString()?.toIntOrNull(),
                            binding.editProfileJobEditText.text?.toString(),
                            binding.editProfileChildCountEditText.text?.toString()?.toIntOrNull(),
                        )
                    )
                )

                viewModel.dataStateUpdateUser.observe(viewLifecycleOwner, {
                    when (it) {
                        is DataState.Error -> {
                            binding.root.showInternetWarningToast()
                            progressBar.hide()
                        }
                        DataState.Loading -> {
                            progressBar.show()
                        }
                        is DataState.RetrofitError -> {
                            binding.root.showInternetWarningToast()
                            progressBar.hide()
                        }
                        is DataState.Success -> {
                            progressBar.hide()
                            sharedPreferences.edit().putString(
                                "userFirstName",
                                binding.editProfileFirstNameEditText.text.toString()
                            ).apply()
                        }
                    }
                })
            }
        }
        binding.editProfileActionBar.onBackClickListener { findNavController().popBackStack() }
        viewModel.setStateEvent(EditProfileEvent.UserInfo)
        viewModel.dataState.observe(viewLifecycleOwner, { dataState ->
            when (dataState) {
                is DataState.Error -> {
                    binding.root.showInternetWarningToast()
                }
                is DataState.Success -> {
                    binding.user = dataState.data?.userInfo
                }
                DataState.Loading -> {
                }
                is DataState.RetrofitError -> {
                    binding.root.showInternetWarningToast()
                }
            }
        })

    }

    private fun checkEmailValidation() {
        binding.editProfileEmailEditText.onTextChangeListener { str ->
            val isError = if (str.isNotEmpty())
                !ValidationRegexChecker.emailValidator(str)
            else
                false
            binding.editProfileEmailTextField.isErrorEnabled = isError
            if (isError)
                binding.editProfileEmailTextField.error = "لطفا ایمیل را صحیح وارد کنید"
        }
    }
}