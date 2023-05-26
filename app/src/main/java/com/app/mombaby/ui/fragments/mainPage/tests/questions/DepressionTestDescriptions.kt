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
import com.app.mombaby.databinding.DepressionTestDescriptionsBinding
import com.app.mombaby.repositories.tests.DepressionTestRepository

class DepressionTestDescriptions : Fragment() {

    lateinit var binding: DepressionTestDescriptionsBinding

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.depression_test_descriptions,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.depressionTestDescriptionsActionBar.onBackClickListener { findNavController().popBackStack() }

        binding.depressionTestDescriptionsButton.setOnClickListener {
            val bundle = bundleOf(
                "repository" to DepressionTestRepository(),
            )
            findNavController().navigate(
                R.id.action_depressionTestDescriptions_to_depressionTestQuestions,
                bundle
            )
        }
    }
}