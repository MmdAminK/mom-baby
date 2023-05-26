package com.app.mombaby.ui.fragments.loginPages.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.mombaby.R
import com.app.mombaby.databinding.ForgetPasswordBinding
import com.app.mombaby.utils.models.DataState
import com.app.mombaby.utils.utilities.ValidationRegexChecker
import com.app.mombaby.utils.utilities.ViewUtils
import com.app.mombaby.utils.utilities.ViewUtils.makeSnackBar
import com.app.mombaby.utils.utilities.ViewUtils.onTextChangeListener
import com.app.mombaby.viewModels.PasswordEvent
import com.app.mombaby.viewModels.PasswordViewModel
import com.app.mombaby.views.AppProgressBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ForgetPassword : Fragment() {

    lateinit var binding: ForgetPasswordBinding

    @Inject
    lateinit var appProgressBar: AppProgressBar

    private val viewModel: PasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.forget_password, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPhoneNumberRegex()
        binding.forgetPasswordSubmitButton.setOnClickListener {
            if (!ViewUtils.isEmptyOrNull(binding.forgetPasswordPhoneEditText)) {
                if (!binding.forgetPasswordPhoneTextField.isErrorEnabled)
                    viewModel.setStateEvent(PasswordEvent.ForgetPassword(binding.forgetPasswordPhoneEditText.text.toString()))

                viewModel.forgetPasswordDataState.observe(viewLifecycleOwner, { dataState ->
                    when (dataState) {
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
                            if (dataState.data?.data == "done")
                                findNavController().navigate(R.id.action_forgetPassword_to_forgetPasswordVerify)
                        }
                    }
                })
            } else
                binding.root.makeSnackBar("لطفا شماره موبایل خود را وارد کنید")
        }

    }

    private fun checkPhoneNumberRegex() {
        binding.apply {
            forgetPasswordPhoneEditText.onTextChangeListener { str ->
                val isError = if (str.isNotEmpty())
                    !ValidationRegexChecker.phoneNumberWithLength(str)
                else
                    false
                forgetPasswordPhoneTextField.isErrorEnabled = isError
                if (isError)
                    forgetPasswordPhoneTextField.error = "لطفا شماره خود را به صورت صحیح وارد کنید"
            }
        }
    }
}