package com.app.mombaby.ui.fragments.loginPages

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.mombaby.R
import com.app.mombaby.databinding.VerifyBinding
import com.app.mombaby.utils.models.CacheKeys
import com.app.mombaby.utils.models.DataState
import com.app.mombaby.utils.utilities.CommonUtils.backToPreviousDestination
import com.app.mombaby.utils.utilities.ViewUtils.backToPreviousDestination
import com.app.mombaby.utils.utilities.ViewUtils.makeSnackBar
import com.app.mombaby.utils.utilities.ViewUtils.onTextChangeListener
import com.app.mombaby.utils.utilities.ViewUtils.showInternetWarningToast
import com.app.mombaby.viewModels.VerifyEvent
import com.app.mombaby.viewModels.VerifyViewModel
import com.app.mombaby.views.AppProgressBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class Verify : Fragment() {

    private val viewModel: VerifyViewModel by viewModels()

    private val time: Long = 59_000
    private val countDown: Long = 1_000
    private val reSendCodeMsg = "ارسال مجدد کد"
    private var phoneNumber: String = ""

    private lateinit var binding: VerifyBinding

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var appProgressBar: AppProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = VerifyBinding.inflate(inflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.verifyCodeTextField.isErrorEnabled = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        phoneNumber = requireArguments().getString("phoneNumber").toString()
        requireActivity().backToPreviousDestination(findNavController())
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()

        binding.verifySubmitButton.setOnClickListener {
            if (!binding.verifyCodeTextField.isErrorEnabled)
                viewModel.setStateEvent(
                    VerifyEvent.Verify(binding.verifyCodeEditText.text.toString(), phoneNumber)
                )
        }

        binding.verifySubTitle.text = "کد ورود به شماره $phoneNumber پیامک شد"
        startTimer()

        binding.verifyChangeNumber.backToPreviousDestination(findNavController())
        binding.verifyCodeEditText.onTextChangeListener { str ->
            val isError = if (str.isNotEmpty()) str.length < 4 else true
            binding.verifyCodeTextField.isErrorEnabled = isError
            if (isError)
                binding.verifyCodeTextField.error = "لطفا کد چهار رقمی را وارد کنید"
        }
    }

    private fun startTimer() {
        object : CountDownTimer(time, countDown) {
            override fun onTick(millisUntilFinished: Long) {
                val timePrefix = if (millisUntilFinished / countDown <= 9) "00:0" else "00:"
                binding.verifyTimer.text =
                    timePrefix.plus(millisUntilFinished / countDown).trimIndent()
                onClickTimer { }
            }

            override fun onFinish() {
                binding.verifyTimer.text = reSendCodeMsg
                onClickTimer { startTimer() }
            }
        }.start()
    }

    private fun onClickTimer(slot: () -> Unit) {
        binding.verifyTimer.setOnClickListener {
            slot()
        }
    }

    private fun observeData() {
        viewModel.dataState.observe(viewLifecycleOwner, { dataState ->
            when (dataState) {
                is DataState.Loading -> {
                    appProgressBar.show()
                }
                is DataState.Success -> {
                    appProgressBar.hide()
                    sharedPreferences.edit().apply {
                        putString(CacheKeys.auth, dataState.data!!.token).apply()
                        putString(CacheKeys.userPhoneNumber, phoneNumber).apply()
                        putString(CacheKeys.userFirstName, dataState.data.firstName).apply()
                    }
                    val bundle = bundleOf("name" to dataState.data?.firstName.isNullOrEmpty())

                    Log.i("TAG", "observeData: ${dataState.data?.passwordCheck}")
                    sharedPreferences.edit()
                        .putBoolean(CacheKeys.passwordEnabled, dataState.data?.passwordCheck != 1)
                        .apply()
                    findNavController().navigate(
                        if (dataState.data?.passwordCheck == 1)
                            if (dataState.data.firstName?.isEmpty()!!)
                                R.id.action_verify_to_signUp
                            else R.id.action_verify_to_pregnancyStatus
                        else
                            if (dataState.data?.appPassword.isNullOrEmpty())
                                R.id.action_verify_to_setPassword
                            else R.id.action_verify_to_passwordLogin, bundle
                    )
                }
                is DataState.Error -> {
                    binding.root.showInternetWarningToast()
                    appProgressBar.hide()
                }
                is DataState.RetrofitError -> {
                    appProgressBar.hide()
                    if (!dataState.errorBody.isNullOrEmpty())
                        binding.root.makeSnackBar("کد تایید اشتباه می باشد")
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearObservers()
    }

}