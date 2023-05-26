package com.app.mombaby.ui.fragments.mainPage.tests.questions

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
import com.app.mombaby.databinding.AnxietyTestDescriptionBinding
import com.app.mombaby.repositories.tests.AnxietyTestRepository

class AnxietyTestDescription : Fragment() {

    lateinit var binding: AnxietyTestDescriptionBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.anxiety_test_description, container, false)
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
        binding.anxietyTestDescriptionsActionBar.onBackClickListener { findNavController().popBackStack() }
        binding.anxietyTestDescriptionsButton.setOnClickListener {
            val bundle = bundleOf(
                "repository" to AnxietyTestRepository(),
            )
            findNavController().navigate(
                R.id.action_anxietyTestDescription_to_testQuestions,
                bundle
            )
        }
    }

}