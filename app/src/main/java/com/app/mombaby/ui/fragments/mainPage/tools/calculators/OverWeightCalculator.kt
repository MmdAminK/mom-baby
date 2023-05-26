package com.app.mombaby.ui.fragments.mainPage.tools.calculators

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.mombaby.R
import com.app.mombaby.databinding.OverWeightCalculatorBinding

class OverWeightCalculator : Fragment() {

    lateinit var binding: OverWeightCalculatorBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.over_weight_calculator, container, false)
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

        binding.overWeightCalculatorActionBar.onBackClickListener { findNavController().popBackStack() }

        binding.overWeightCalculatorButton.setOnClickListener {
            val bundle = bundleOf(
                "weight" to binding.overWeightCalculatorWeightSlider.value,
                "height" to binding.overWeightCalculatorHeightSlider.value
            )
            findNavController().navigate(
                R.id.action_overWeightCalculator_to_overWeightResult,
                bundle
            )
        }
    }
}