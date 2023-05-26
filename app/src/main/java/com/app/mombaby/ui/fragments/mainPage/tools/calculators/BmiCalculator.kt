package com.app.mombaby.ui.fragments.mainPage.tools.calculators

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.mombaby.R
import com.app.mombaby.databinding.BmiCalculatorBinding
import com.app.mombaby.utils.utilities.CommonUtils.backToPreviousDestination

class BmiCalculator : Fragment() {

    lateinit var binding: BmiCalculatorBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.bmi_calculator, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().backToPreviousDestination(findNavController())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bmiCalculatorActionBar.onBackClickListener { findNavController().popBackStack() }

        binding.bmiCalculatorButton.setOnClickListener {
            val bundle = bundleOf(
                "weight" to binding.bmiWeightCalculatorSlider.value,
                "height" to binding.bmiHeightCalculatorSlider.value
            )
            findNavController().navigate(R.id.action_bmiCalculator_to_bmiResult, bundle)
        }
    }
}