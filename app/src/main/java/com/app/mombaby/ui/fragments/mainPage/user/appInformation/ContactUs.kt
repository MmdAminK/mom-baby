package com.app.mombaby.ui.fragments.mainPage.user.appInformation

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.mombaby.R
import com.app.mombaby.databinding.ContactUsBinding
import com.app.mombaby.utils.models.DataState
import com.app.mombaby.utils.utilities.ValidationRegexChecker
import com.app.mombaby.utils.utilities.ViewUtils
import com.app.mombaby.utils.utilities.ViewUtils.makeSnackBar
import com.app.mombaby.utils.utilities.ViewUtils.onTextChangeListener
import com.app.mombaby.utils.utilities.ViewUtils.showInternetWarningToast
import com.app.mombaby.viewModels.AppInfoEvent
import com.app.mombaby.viewModels.AppInformationViewModel
import com.app.mombaby.views.AppProgressBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ContactUs : Fragment() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var progressBar: AppProgressBar

    lateinit var binding: ContactUsBinding

    val viewModel: AppInformationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.contact_us, container, false)
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

        checkPhoneNumberRegex()
        binding.contactUsSendLink.setOnClickListener {
            if (ViewUtils.isEmptyOrNull(
                    binding.contactUsNameEditText,
                    binding.contactUsPhoneEditText,
                    binding.contactUsUrMessageEditText
                )
            ) {
                binding.root.makeSnackBar("لطفا تمامی موارد را کامل کنید")
            } else {
                if (!binding.contactUsPhoneTextField.isErrorEnabled) {
                    viewModel.setStateEvent(
                        AppInfoEvent.SendMessage(
                            binding.contactUsNameEditText.text.toString(),
                            binding.contactUsPhoneEditText.text.toString(),
                            binding.contactUsUrMessageEditText.text.toString()
                        )
                    )
                    viewModel.dataStateSendMessage.observe(viewLifecycleOwner) {
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
                                binding.contactUsNameEditText.text?.clear()
                                binding.contactUsPhoneEditText.text?.clear()
                                binding.contactUsUrMessageEditText.text?.clear()
                                Toast.makeText(
                                    requireContext(),
                                    "پیام با موفقیت ارسال شد",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }
                    }
                }

            }
        }
        binding.contactUsAppActionBar.onBackClickListener { findNavController().popBackStack() }
        viewModel.setStateEvent(AppInfoEvent.ContactUs)
        viewModel.dataStateContactUs.observe(viewLifecycleOwner, {
            when (it) {
                is DataState.Error -> {
                    binding.root.showInternetWarningToast()
                }
                DataState.Loading -> {
                }
                is DataState.RetrofitError -> {
                    binding.root.showInternetWarningToast()
                }
                is DataState.Success -> {
                    val description = sharedPreferences.getString("contactUs", "")
                    if (description == "")
                        binding.contactUsDescription.text = it.data?.contact
                    else
                        binding.contactUsDescription.text = description
                }
            }
        })

    }

    private fun checkPhoneNumberRegex() {
        binding.apply {
            contactUsPhoneEditText.onTextChangeListener { str ->
                val isError = if (str.isNotEmpty())
                    !ValidationRegexChecker.phoneNumberWithLength(str)
                else
                    false
                contactUsPhoneTextField.isErrorEnabled = isError
                if (isError)
                    contactUsPhoneTextField.error = "لطفا شماره خود را به صورت صحیح وارد کنید"
            }
        }
    }
}
