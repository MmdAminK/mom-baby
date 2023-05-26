package com.app.mombaby.ui.fragments.mainPage.tools.results

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.mombaby.R
import com.app.mombaby.databinding.BmiResultBinding
import java.text.DecimalFormat
import kotlin.math.pow


class BmiResult : Fragment() {

    lateinit var binding: BmiResultBinding

    var height: Int = 0
    var weight: Int = 0
    var bmi: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        height = requireArguments().getInt("height")
        weight = requireArguments().getInt("weight")
        bmi = (weight.toDouble() / ((height.toDouble() / 100).pow(2.0)))

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
        binding = DataBindingUtil.inflate(inflater, R.layout.bmi_result, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bmiResultActionBar.onBackClickListener { findNavController().popBackStack() }

        binding.bmiResultHeight.text = "$height سانتی متر"
        binding.bmiResultWeight.text = "$weight کیلوگرم"
        binding.bmiResultText.text = DecimalFormat("##.##").format(bmi)

        var constraintPercent = 0f
        constraintPercent = if (bmi.toFloat() > 34) {
            (34f - 15f) / 20
        } else
            (((bmi.toFloat() - 15f) / 20.8).toFloat())
        val set = ConstraintSet();

        set.clone(binding.bmiResultSignConstraint)
        set.constrainPercentWidth(R.id.bmi_result_sign_linear, constraintPercent)
        set.applyTo(binding.bmiResultSignConstraint)

    }
}