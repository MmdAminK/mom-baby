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
import com.app.mombaby.databinding.GestationalAgeCalculatorBinding
import org.joda.time.DateTime
import org.joda.time.Days

class GestationalAgeCalculator : Fragment() {

    lateinit var binding: GestationalAgeCalculatorBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.gestational_age_calculator, container, false)
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

        binding.gestationalAgeAppActionBar.onBackClickListener { findNavController().popBackStack() }

        binding.gestationalAgeCalculatorButton.setOnClickListener {
            binding.gestationalAgeCalendar.selectedDate?.let { dateTime ->
                val bundle = bundleOf(
                    "date" to showDate(dateTime),
                    "gestationalAge" to
                            createGestationalAgeStatus(
                                (Days.daysBetween(
                                    dateTime,
                                    binding.gestationalAgeCalendar.initDate()
                                ).days)
                            )
                )
                findNavController().navigate(
                    R.id.action_gestationalAgeCalculator_to_gestationalAgeResult,
                    bundle
                )
            } ?: run {
                Toast.makeText(requireContext(), "لطفا تاریخ را وارد کنید", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }

    private fun showDate(dateTime: DateTime): String {
        return "${dateTime.dayOfMonth} ${
            binding.gestationalAgeCalendar.monthNameAndYear(
                dateTime
            )
        }"
    }

    private fun createGestationalAgeStatus(totalDays: Int): String {
        val weeks: Int = totalDays / 7
        val days: Int = totalDays % 7
        return if (totalDays > 0)
            if (weeks != 0) "$weeks هفته  و $days روز " else "$days روز "
        else
            "لطفا تاریخ را صحیح وارد کنید"
    }
}