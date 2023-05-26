package com.app.mombaby.ui.fragments.mainPage.tools.calculators

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.mombaby.R
import com.app.mombaby.databinding.BabyBirthdayCalculatorBinding
import org.joda.time.DateTime

class BabyBirthDayCalculator : Fragment() {

    lateinit var binding: BabyBirthdayCalculatorBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.baby_birthday_calculator, container, false)
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
        binding.babyBirthDayAppActionBar.onBackClickListener { findNavController().popBackStack() }

        binding.babyBirthDayCalculatorButton.setOnClickListener {
            binding.babyBirthDayCalendar.selectedDate?.let { dateTime ->
                val bundle = bundleOf(
                    "date" to showDate(dateTime),
                    "birthDay" to showDate(dateTime.plusDays(279))
                )
                findNavController().navigate(
                    R.id.action_babyBirthDayCalculator_to_babyBirthDayResult,
                    bundle
                )
            } ?: run {
                Toast.makeText(requireContext(), "لطفا تاریخ را وارد کنید", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun showDate(dateTime: DateTime): String {
        return "${dateTime.dayOfMonth} ${binding.babyBirthDayCalendar.monthNameAndYear(dateTime)}"
    }
}