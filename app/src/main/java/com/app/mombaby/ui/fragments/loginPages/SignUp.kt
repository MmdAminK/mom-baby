package com.app.mombaby.ui.fragments.loginPages

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.mombaby.R
import com.app.mombaby.databinding.SignUpBinding
import com.app.mombaby.models.signup.UserReqBody
import com.app.mombaby.utils.models.CacheKeys
import com.app.mombaby.utils.models.DataState
import com.app.mombaby.utils.utilities.CommonUtils.onBackPressed
import com.app.mombaby.utils.utilities.ValidationRegexChecker
import com.app.mombaby.utils.utilities.ViewUtils
import com.app.mombaby.utils.utilities.ViewUtils.makeSnackBar
import com.app.mombaby.utils.utilities.ViewUtils.onTextChangeListener
import com.app.mombaby.utils.utilities.ViewUtils.showInternetWarningToast
import com.app.mombaby.viewModels.SignUpEvent
import com.app.mombaby.viewModels.SingUpViewModel
import com.app.mombaby.views.AppProgressBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SignUp : Fragment() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var appProgressBar: AppProgressBar

    private val viewModel: SingUpViewModel by viewModels()
    lateinit var binding: SignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SignUpBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressed { }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUpLater.setOnClickListener {
            findNavController().navigate(R.id.action_signUp_to_pregnancyStatus)
        }

        checkEmailValidation()
        observeData()

        binding.apply {
            signUpSubmit.setOnClickListener {
                if (!ViewUtils.isEmptyOrNull(
                        signUpFirstNameEditText,
                        signUpLastNameEditText,
                        signUpEmailEditText,
                        signUpAgeEditText
                    )
                ) {
                    if (!signUpFirstNameTextField.isErrorEnabled && !signUpLastNameTextField.isErrorEnabled &&
                        !signUpAgeTextField.isErrorEnabled && !signUpEmailTextField.isErrorEnabled
                    ) {
                        viewModel.setStateEvent(
                            SignUpEvent.SigningUp(
                                UserReqBody(
                                    binding.signUpFirstNameEditText.text?.toString(),
                                    binding.signUpLastNameEditText.text?.toString(),
                                    binding.signUpEmailEditText.text?.toString(),
                                    binding.signUpAgeEditText.text?.toString()?.toIntOrNull(),
                                )
                            )
                        )
                        sharedPreferences.edit().putString(
                            CacheKeys.userFirstName,
                            binding.signUpFirstNameEditText.text?.toString()
                        ).apply()
                    }
                } else {
                    binding.root.makeSnackBar("لطفا تمامی موارد را کامل کنید")
                }
            }
        }
    }

    private fun observeData() {
        viewModel.dataState.observe(viewLifecycleOwner, {
            when (it) {
                is DataState.Loading -> {
                    appProgressBar.show()
                }
                is DataState.Success -> {
                    appProgressBar.hide()
                    if (it.data?.data == "done")
                        findNavController().navigate(R.id.action_signUp_to_pregnancyStatus)
                }
                is DataState.RetrofitError -> {
                    binding.root.showInternetWarningToast()
                    appProgressBar.hide()
                }
                is DataState.Error -> {
                    binding.root.showInternetWarningToast()
                    appProgressBar.hide()
                }
            }
        })
    }

    private fun checkEmailValidation() {
        binding.signUpEmailEditText.onTextChangeListener { str ->
            val isError = if (str.isNotEmpty())
                !ValidationRegexChecker.emailValidator(str)
            else
                false
            binding.signUpEmailTextField.isErrorEnabled = isError
            if (isError)
                binding.signUpEmailTextField.error = "لطفا ایمیل را صحیح وارد کنید"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearObservers()
    }

}