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
import com.app.mombaby.databinding.OverWeightResultBinding
import java.text.DecimalFormat
import kotlin.math.pow

class OverWeightResult : Fragment() {


    lateinit var binding: OverWeightResultBinding

    var height: Int = 0
    var weight: Int = 0
    var bmi: Double = 0.0
    var overWeightStatus: String = ""
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
        binding = DataBindingUtil.inflate(inflater, R.layout.over_weight_result, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.overWeightResultActionBar.onBackClickListener { findNavController().popBackStack() }

        binding.overWeightResultHeight.text = "$height سانتی متر"
        binding.overWeightResultWeight.text = "$weight کیلوگرم"
        DecimalFormat("##.##").format(bmi)

        binding.overWeightResultText.apply {
            when {
                bmi < 18.5 -> text = createOverWeightStatus(12.5f, 18f, " مجاز به اضافه وزن هستید")
                18.5 < bmi && bmi <= 25 -> text =
                    createOverWeightStatus(11.5f, 16f, " مجاز به اضافه وزن هستید")
                25 < bmi && bmi <= 30 -> text =
                    createOverWeightStatus(7f, 11.5f, " مجاز به اضافه وزن هستید")
                30 < bmi -> text = createOverWeightStatus(5f, 9f, " مجاز به اضافه وزن هستید")
            }
        }


    }

    private fun createOverWeightStatus(start: Float, end: Float, message: String = "") =
        " $start تا $end کیلوگرم $message "
}