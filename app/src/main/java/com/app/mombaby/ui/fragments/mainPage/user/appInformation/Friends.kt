package com.app.mombaby.ui.fragments.mainPage.user.appInformation

import android.content.Intent
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
import com.app.mombaby.databinding.FriendsBinding
import com.app.mombaby.utils.models.DataState
import com.app.mombaby.utils.utilities.ViewUtils.showInternetWarningToast
import com.app.mombaby.viewModels.AppInfoEvent
import com.app.mombaby.viewModels.AppInformationViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class Friends : Fragment() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    lateinit var binding: FriendsBinding

    val vieModel: AppInformationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.friends, container, false)
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

        binding.friendsAppActionBar.onBackClickListener { findNavController().popBackStack() }
        binding.friendsSendLink.setOnClickListener {
            vieModel.setStateEvent(AppInfoEvent.Friends)
            vieModel.dataStateFriends.observe(viewLifecycleOwner, {
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

                        val iosLink = sharedPreferences.getString("iosAppLink", "")
                        val androidLink = sharedPreferences.getString("androidAppLink", "")
                        var linkText = ""
                        //todo make func
                        linkText = if (iosLink.isNullOrEmpty() || androidLink.isNullOrEmpty())
                            "لینک اپلیکیشن ios : " + "\n" + it.data?.iosAppLink + "\n" + "لینک اپلیکیشن اندروید : " + "\n" + it.data?.androidAppLink
                        else
                            "لینک اپلیکیشن ios : \n$iosLink\n لینک اپلیکیشن اندروید : \n$androidLink"

                        val shareIntent = Intent(Intent.ACTION_SEND)
                        shareIntent.type = "text/plain"
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "مام بیبی")
                        shareIntent.putExtra(Intent.EXTRA_TEXT, linkText)
                        startActivity(Intent.createChooser(shareIntent, "انتخاب کن"))
                    }
                }
            })
        }

    }
}
