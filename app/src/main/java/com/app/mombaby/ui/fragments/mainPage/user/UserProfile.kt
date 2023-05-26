package com.app.mombaby.ui.fragments.mainPage.user

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.mombaby.R
import com.app.mombaby.databinding.UserProfileBinding
import com.app.mombaby.models.appInfo.ContactUsDto
import com.app.mombaby.ui.fragments.loginPages.PregnancyStatus
import com.app.mombaby.utils.models.CacheKeys
import com.app.mombaby.utils.models.CacheKeys.contactUs
import com.app.mombaby.utils.models.CacheKeys.userFirstName
import com.app.mombaby.utils.models.CacheKeys.userPhoneNumber
import com.app.mombaby.utils.models.DataState
import com.app.mombaby.utils.utilities.AppAlertDialog
import com.app.mombaby.utils.utilities.CommonUtils.onBackPressed
import com.app.mombaby.utils.utilities.CommonUtils.openLinkInBrowser
import com.app.mombaby.utils.utilities.TapTargetBuilder.createTapTarget
import com.app.mombaby.utils.utilities.ViewUtils.showInternetWarningToast
import com.app.mombaby.viewModels.AppInfoEvent
import com.app.mombaby.viewModels.AppInformationViewModel
import com.app.mombaby.viewModels.PregnancyStatusEvent
import com.app.mombaby.viewModels.PregnancyStatusViewModel
import com.app.mombaby.views.AppProgressBar
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class UserProfile : Fragment() {

    private val appInformationViewModel: AppInformationViewModel by viewModels()
    val pregnancyStatusViewModel: PregnancyStatusViewModel by viewModels()

    @Inject
    lateinit var progressBar: AppProgressBar

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    lateinit var binding: UserProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.user_profile, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressed {
            val dialog = AppAlertDialog(requireContext())
            dialog.setMessage("آیا می خواهید از برنامه خارج شوید؟")
            dialog.setYesButtonAction {
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_HOME)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            dialog.setNoButtonAction { }
            dialog.showDialog()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!sharedPreferences.getBoolean(CacheKeys.alarmTapTarget, false)) {
            TapTargetSequence(requireActivity()).target(
                requireActivity().createTapTarget(
                    6,
                    binding.alarmTxt,
                    "یادآوری",
                    "اینجا میتونی زمان مصرف قرص، معاینات، آزمایش ها و... رو ثبت کنی تا نرم افزار بهت زمان انجام اونا رو یادآوری کنه."
                )
            )
                .listener(object : TapTargetSequence.Listener {
                    override fun onSequenceFinish() {
                    }

                    override fun onSequenceStep(
                        lastTarget: TapTarget?,
                        targetClicked: Boolean
                    ) {
                        sharedPreferences.edit()
                            .putBoolean(CacheKeys.alarmTapTarget, true)
                            .apply()
                    }

                    override fun onSequenceCanceled(lastTarget: TapTarget?) {
                    }
                })
                .start()
        }

        val phoneNumber = sharedPreferences.getString(userPhoneNumber, "")
        binding.userProfilePhoneNumber.text = phoneNumber

        val userFirstName = sharedPreferences.getString(userFirstName, "")
        if (userFirstName != "")
            binding.userProfileName.text = userFirstName
        else
            binding.userProfileName.text = "مشخص نشده"


        binding.userProfileRules.setOnClickListener { findNavController().navigate(R.id.action_userProfile_to_rules) }
        binding.userProfileContactUs.setOnClickListener { findNavController().navigate(R.id.action_userProfile_to_contactUs) }
        binding.userProfileFriends.setOnClickListener { findNavController().navigate(R.id.action_userProfile_to_friends) }
        binding.userProfileAboutUs.setOnClickListener { findNavController().navigate(R.id.action_userProfile_to_aboutUs) }
        binding.userProfilePassword.setOnClickListener { findNavController().navigate(R.id.action_userProfile_to_profilePassword) }

        binding.notPregnantLinear.setOnClickListener {
            binding.userProfileStickySwitch.isOn = false
            setIsOn(false)
        }

        binding.pregnantLinear.setOnClickListener {
            binding.userProfileStickySwitch.isOn = true
            setIsOn(true)
        }
        binding.profileReminder.setOnClickListener {
            findNavController().navigate(R.id.action_userProfile_to_reminder)
        }

        binding.userProfileEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_userProfile_to_editProfile)
        }

        binding.instagram.setOnClickListener {
            val contactUs = sharedPreferences.getString(contactUs, "")
            if (contactUs != "") {
                val instagramLink = Gson().fromJson(contactUs, ContactUsDto::class.java).instagram
                requireActivity().openLinkInBrowser(instagramLink)
            }
        }

        binding.whatsApp.setOnClickListener {
            val contactUs = sharedPreferences.getString(contactUs, "")
            if (contactUs != "") {
                val whatsAppNumber = Gson().fromJson(contactUs, ContactUsDto::class.java).whatsapp
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$whatsAppNumber")
                startActivity(intent)
            }
        }

        binding.telegram.setOnClickListener {
            val contactUs = sharedPreferences.getString(contactUs, "")
            if (contactUs != "") {
                val telegramLink = Gson().fromJson(contactUs, ContactUsDto::class.java).telegram
                requireActivity().openLinkInBrowser(telegramLink)
            }
        }

        binding.twitter.setOnClickListener {
            val contactUs = sharedPreferences.getString(contactUs, "")
            if (contactUs != "") {
                val twitterLink = Gson().fromJson(contactUs, ContactUsDto::class.java).twitter
                requireActivity().openLinkInBrowser(twitterLink)
            }
        }

        binding.userProfileStickySwitch.isOn = sharedPreferences.getInt(
            CacheKeys.status, 1
        ) == PregnancyStatus.Status.Pregnancy.value

        binding.userProfileStickySwitch.setOnToggledListener { _, isOn ->
            setIsOn(isOn)
        }

        appInformationViewModel.setStateEvent(AppInfoEvent.ContactUs)
        appInformationViewModel.dataStateContactUs.observe(viewLifecycleOwner, { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    sharedPreferences.edit().putString(contactUs, Gson().toJson(dataState.data))
                        .apply()
                }
                is DataState.Error -> {
                    binding.root.showInternetWarningToast()
                }
                is DataState.Loading -> {
                }
                is DataState.RetrofitError -> {
                    binding.root.showInternetWarningToast()
                }
            }
        })
    }

    private fun setIsOn(isOn: Boolean) {
        val stickValue = if (isOn) 2 else 1
        pregnancyStatusViewModel.setStateEvent(PregnancyStatusEvent.UpdateStatus(stickValue))
        pregnancyStatusViewModel.dataState.observe(viewLifecycleOwner, {
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
                    sharedPreferences.edit().putInt(CacheKeys.status, stickValue).apply()
                }
            }
        })
    }

}