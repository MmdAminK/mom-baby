package com.app.mombaby.ui.fragments.mainPage.tools.calculators

import android.os.Bundle
import android.util.Log
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
import com.app.mombaby.databinding.OvulationDayCalculatorBinding
import org.joda.time.DateTime

class OvulationDayCalculator : Fragment() {

    private lateinit var binding: OvulationDayCalculatorBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.ovulation_day_calculator, container, false)
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

        binding.ovulationDayAppActionBar.onBackClickListener { findNavController().popBackStack() }

        binding.ovulationDayResultCalculatorButton.setOnClickListener {

            val date = binding.ovulationDayResultCalendar.selectedDate
            val between = binding.ovulationDayDaySliderBetweenBlood.value
            val during = binding.ovulationDayDaySliderBlood.value

            val nextPeriodDate = date?.plusDays(between)
            val nextPeriodDate2 = nextPeriodDate?.plusDays(between)
            val nextPeriodDate3 = nextPeriodDate2?.plusDays(between)


            val ovulation1 = nextPeriodDate?.minusDays(16)
            val ovulation2 = nextPeriodDate?.minusDays(12)

            val ovulation3 = nextPeriodDate2?.minusDays(16)
            val ovulation4 = nextPeriodDate2?.minusDays(12)

            val ovulation5 = nextPeriodDate3?.minusDays(16)
            val ovulation6 = nextPeriodDate3?.minusDays(12)

            Log.i(
                "TAG",
                "onViewCreated:\n ${showDate(ovulation1!!)}  \n  ${showDate(ovulation2!!)}!  " +
                        "onViewCreated:\n ${showDate(ovulation3!!)}  \n  ${showDate(ovulation4!!)}!  " +
                        "onViewCreated:\n ${showDate(ovulation5!!)}  \n  ${showDate(ovulation6!!)}!  "
            )




            binding.ovulationDayResultCalendar.selectedDate?.let { dateTime ->
                val bundle = bundleOf(
                    "date" to showDate(dateTime),
                    "blood" to "${binding.ovulationDayDaySliderBlood.value} روز",
                    "ovulationDay" to "${showDate(ovulation1)} تا ${showDate(ovulation2)} \n\n" +
                            "${showDate(ovulation3)} تا ${showDate(ovulation4)} \n\n" +
                            "${showDate(ovulation5)} تا ${showDate(ovulation6)}"
                )
                findNavController().navigate(
                    R.id.action_ovulationDay_to_ovulationDayResult, bundle
                )
            } ?: run {
                Toast.makeText(requireContext(), "لطفا تاریخ را وارد کنید", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    //Todo: Move to viewModel for all tools' fragments
    private fun calculateNextMenstruationDay(dateTime: DateTime): DateTime {
        binding.apply {
            return dateTime.plusDays(ovulationDayDaySliderBetweenBlood.value)
//                .plusDays(ovulationDayDaySliderBlood.value)
        }
    }

    private fun createOvulationDayStatus(dateTime: DateTime): String {
        calculateNextMenstruationDay(dateTime).let {
            return "${showDate(it.minusDays(16))} تا ${showDate(it.minusDays(12))}"
        }
    }

    private fun showDate(dateTime: DateTime): String {
        return "${dateTime.dayOfMonth} ${
            binding.ovulationDayResultCalendar.monthNameAndYear(
                dateTime
            )
        }"
    }
}