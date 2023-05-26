package com.app.mombaby.ui.fragments.mainPage.home

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.mombaby.R
import com.app.mombaby.databinding.HomeBinding
import com.app.mombaby.ui.fragments.loginPages.PregnancyStatus
import com.app.mombaby.utils.models.CacheKeys
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Home : Fragment() {

    lateinit var binding: HomeBinding

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.home, container, false)
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (sharedPreferences.getInt(
                CacheKeys.status,
                1
            ) == PregnancyStatus.Status.Menstruation.value
        )
            findNavController().navigate(R.id.action_home_to_menstruationHome)
        else
            findNavController().navigate(R.id.action_home_to_pregnancyHome)

    }


}