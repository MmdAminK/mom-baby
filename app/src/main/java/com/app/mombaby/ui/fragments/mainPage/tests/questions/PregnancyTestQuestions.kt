package com.app.mombaby.ui.fragments.mainPage.tests.questions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.mombaby.R
import com.app.mombaby.databinding.PregnancyTestQuestionsBinding
import com.app.mombaby.ui.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class PregnancyTestQuestions : Fragment() {

    lateinit var binding: PregnancyTestQuestionsBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter

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
        binding =
            DataBindingUtil.inflate(inflater, R.layout.pregnancy_test_questions, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pregnancyTestActionBar.onBackClickListener { findNavController().popBackStack() }

        viewPagerAdapter = ViewPagerAdapter(requireActivity())

        viewPagerAdapter.addFragment(BloodTestQuestions())
        viewPagerAdapter.addFragment(BabyCheckQuestions())

        binding.pregnancyTestPager.adapter = viewPagerAdapter


        binding.pregnancyTestTabLayout.apply { selectTab(getTabAt(0)) }
        TabLayoutMediator(
            binding.pregnancyTestTabLayout,
            binding.pregnancyTestPager
        ) { tab, position ->
            when (position) {
                0 -> tab.text = "تست خون"
                1 -> tab.text = "بیبی چک"
            }
        }.attach()

    }
}