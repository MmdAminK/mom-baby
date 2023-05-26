package com.app.mombaby.ui.fragments.mainPage.tools.results

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.mombaby.R
import com.app.mombaby.databinding.BabyBirthDayResultBinding

class BabyBirthDayResult : Fragment() {


    lateinit var binding: BabyBirthDayResultBinding

    var date: String = ""
    var birthDay: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        date = requireArguments().getString("date").toString()
        birthDay = requireArguments().getString("birthDay").toString()

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.baby_birth_day_result, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.babyBirthDayResultActionBar.onBackClickListener { findNavController().popBackStack() }

        binding.babyBirthDayResultDate.text = date
        binding.babyBirthDayResultText.text = birthDay

    }


}