package com.app.mombaby.ui.fragments.mainPage.user.appInformation

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
import com.app.mombaby.databinding.RulesBinding
import com.app.mombaby.utils.models.DataState
import com.app.mombaby.utils.utilities.ViewUtils.showInternetWarningToast
import com.app.mombaby.viewModels.AppInfoEvent
import com.app.mombaby.viewModels.AppInformationViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Rules : Fragment() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    lateinit var binding: RulesBinding

    val vieModel: AppInformationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.rules, container, false)
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

        binding.rulesActionBar.onBackClickListener { findNavController().popBackStack() }
        vieModel.setStateEvent(AppInfoEvent.Rules)
        vieModel.dataStateRules.observe(viewLifecycleOwner, {
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
                    val description = sharedPreferences.getString("rules", "")
                    if (description == "")
                        binding.rulesDescription.text = it.data?.rules?.joinToString("\n")
                    else
                        binding.rulesDescription.text = description
                }
            }
        })

    }
}