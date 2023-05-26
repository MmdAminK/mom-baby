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
import com.app.mombaby.databinding.OvulationDayResultBinding
import com.app.mombaby.views.AppProgressBar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class OvulationDayResult : Fragment() {


    lateinit var binding: OvulationDayResultBinding

    @Inject
    lateinit var progressBar: AppProgressBar

    var date: String = ""
    var blood: String = ""
    var ovulationDay: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        date = requireArguments().getString("date").toString()
        blood = requireArguments().getString("blood").toString()
        ovulationDay = requireArguments().getString("ovulationDay").toString()

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
            DataBindingUtil.inflate(inflater, R.layout.ovulation_day_result, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ovulationDayResultActionBar.onBackClickListener { findNavController().popBackStack() }
        
        binding.ovulationDayResultDate.text = date
        binding.ovulationDayResultBlood.text = blood
        binding.ovulationDayResultText.text = ovulationDay

    }
}