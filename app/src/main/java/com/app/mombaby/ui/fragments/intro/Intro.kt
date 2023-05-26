package com.app.mombaby.ui.fragments.intro

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.mombaby.R
import com.app.mombaby.databinding.IntroBinding
import com.app.mombaby.models.intro.Intro
import com.app.mombaby.models.intro.IntroUiModel
import com.app.mombaby.ui.adapters.ViewPagerAdapter
import com.app.mombaby.utils.animations.ZoomOutPageTransformer
import com.app.mombaby.utils.models.CacheKeys
import com.app.mombaby.utils.utilities.CommonUtils.onBackPressed
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Intro : Fragment() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var pagerAdapter: ViewPagerAdapter
    lateinit var binding: IntroBinding

    lateinit var introData: Intro

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.intro, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        introData = requireArguments().get("introData") as Intro
        requireActivity().onBackPressed { }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pagerAdapter = ViewPagerAdapter(requireActivity())
        createIntroFragmentList()
        binding.introViewPager.adapter = pagerAdapter
        binding.introViewPager.setPageTransformer(ZoomOutPageTransformer())
    }

    private fun createIntroFragmentList() {
        pagerAdapter.apply {
            addFragment(makeFirstFragment())
            addFragment(makeSecondFragment())
            addFragment(makeThirdFragment())
        }
    }

    private fun makeFirstFragment(): IntroSlide {
        introData.let {
            return IntroSlide(
                IntroUiModel(
                    it.firstTitle, it.firstDescription, R.drawable.intro_image_1,
                    isFirstPage = true,
                    onNextClick = {
                        binding.introViewPager.currentItem = 1
                    })
            )
        }
    }

    private fun makeSecondFragment(): IntroSlide {
        introData.let {
            return IntroSlide(
                IntroUiModel(it.secondTitle,
                    it.secondDescription, R.drawable.intro_image_1, page = 2,
                    onNextClick = {
                        binding.introViewPager.currentItem = 2
                    },
                    onPreviousClick = {
                        binding.introViewPager.currentItem = 0
                    })
            )
        }
    }

    private fun makeThirdFragment(): IntroSlide {
        introData.let {
            return IntroSlide(
                IntroUiModel(
                    it.thirdTitle, it.thirdDescription, R.drawable.intro_image_3,
                    page = 3, isLastPage = true,
                    onNextClick = {
                        sharedPreferences.edit().putBoolean(CacheKeys.introSeen, true).apply()
                        findNavController().navigate(R.id.action_intro_to_login)
                    },
                    onPreviousClick = {
                        binding.introViewPager.currentItem = 1
                    })
            )
        }
    }
}