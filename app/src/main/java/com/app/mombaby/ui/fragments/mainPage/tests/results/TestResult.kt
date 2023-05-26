package com.app.mombaby.ui.fragments.mainPage.tests.results

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
import com.app.mombaby.databinding.TestResultBinding

class TestResult : Fragment() {

    lateinit var binding: TestResultBinding
    var result: String = ""
    var title: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = requireArguments().getString("title").toString()
        result = requireArguments().getString("result").toString()

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
        binding = DataBindingUtil.inflate(inflater, R.layout.test_result, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.testResultActionBar.onBackClickListener { findNavController().popBackStack() }
        binding.testResultTestAgain.setOnClickListener { findNavController().popBackStack() }
        binding.apply {
            testResultActionBar.setTitle(title)
            testResultTitle.text = "نتیجه $title شما"
            testResultText.text = result
            testResultActionBar.onBackClickListener {
                findNavController().navigate(R.id.action_testResult_to_tests)
            }
        }

    }
}