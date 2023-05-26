package com.app.mombaby.ui.fragments.loginPages

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
import com.app.mombaby.databinding.LoginBinding
import com.app.mombaby.utils.models.DataState
import com.app.mombaby.utils.utilities.CaptchaCreator
import com.app.mombaby.utils.utilities.CommonUtils.onBackPressed
import com.app.mombaby.utils.utilities.ValidationRegexChecker
import com.app.mombaby.utils.utilities.ViewUtils
import com.app.mombaby.utils.utilities.ViewUtils.makeSnackBar
import com.app.mombaby.utils.utilities.ViewUtils.onTextChangeListener
import com.app.mombaby.utils.utilities.ViewUtils.showInternetWarningToast
import com.app.mombaby.viewModels.LoginEvent
import com.app.mombaby.viewModels.LoginViewModel
import com.app.mombaby.views.AppProgressBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class Login : Fragment() {

    private lateinit var binding: LoginBinding
    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var appProgressBar: AppProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressed { }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.login, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        clearCaptchaCode()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLoginDate()

        binding.loginTerms.setOnClickListener { findNavController().navigate(R.id.action_login_to_rules2) }
        binding.loginCaptcha.setImageBitmap(CaptchaCreator.instance!!.createBitmap());
        binding.loginSubmitButton.setOnClickListener {
            binding.apply {
                if (!ViewUtils.isEmptyOrNull(loginPhoneEditText, loginCaptchaCode)) {
                    if (!loginPhoneTextField.isErrorEnabled && !loginCaptchaTextField.isErrorEnabled)
                        viewModel.setStateEvent(LoginEvent.Login(loginPhoneEditText.text.toString()))
                } else
                    binding.root.makeSnackBar("لطفا تمامی موارد را کامل کنید")
            }
        }

        checkPhoneNumberRegex()
        checkCaptchaCode()
        binding.loginCaptcha.setOnClickListener {
            binding.loginCaptcha.setImageBitmap(CaptchaCreator.instance!!.createBitmap());
            clearCaptchaCode()
        }
    }

    private fun observeLoginDate() {
        viewModel.dataState.observe(viewLifecycleOwner) { dataState ->
            when (dataState) {
                is DataState.Loading -> {
                    appProgressBar.show()
                }
                is DataState.Success -> {
                    appProgressBar.hide()
                    val bundle = bundleOf("phoneNumber" to dataState.data.mobile)
                    findNavController().navigate(R.id.action_login_to_verify, bundle)
                }
                is DataState.Error -> {
                    binding.root.showInternetWarningToast()
                    appProgressBar.hide()
                }
                is DataState.RetrofitError -> {
                    binding.root.showInternetWarningToast()
                }
            }
        }

    }

    private fun checkCaptchaCode() {
        binding.apply {
            loginCaptchaCode.onTextChangeListener { str ->
                val isError = str != CaptchaCreator.instance!!.code
                loginCaptchaTextField.isErrorEnabled = isError
                if (isError) loginCaptchaTextField.error = "کد تصویر را صحیح وارد کنید"
            }
        }
    }

    private fun checkPhoneNumberRegex() {
        binding.apply {
            loginPhoneEditText.onTextChangeListener { str ->
                val isError = if (str.isNotEmpty())
                    !ValidationRegexChecker.phoneNumberWithLength(str)
                else
                    false
                loginPhoneTextField.isErrorEnabled = isError
                if (isError)
                    loginPhoneTextField.error = "لطفا شماره خود را به صورت صحیح وارد کنید"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearObservers()
    }

    private fun clearCaptchaCode() {
        binding.loginCaptchaCode.text?.clear()
        binding.loginCaptchaTextField.isErrorEnabled = false
    }
}