package com.app.mombaby.ui.fragments.intro

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.app.mombaby.R
import com.app.mombaby.databinding.IntroSlideBinding
import com.app.mombaby.models.intro.IntroUiModel
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IntroSlide
constructor(
    private val introUiModel: IntroUiModel,
) : Fragment() {

    private lateinit var binding: IntroSlideBinding
    private val circleImageArray: ArrayList<ImageView> = ArrayList()

    @Inject
    lateinit var requestManager: RequestManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.intro_slide, container, false)
        binding.apply {
            circleImageArray.add(introSlideFirstCircle)
            circleImageArray.add(introSlideSecondCircle)
            circleImageArray.add(introSlideThirdCircle)
        }
        binding.introUiModel = introUiModel
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        circleVisibility(introUiModel.page)
        previousAndNextInit(introUiModel)
        initSlidesImageView()
        initClickListeners()
    }

    private fun initClickListeners() {
        introUiModel.onNextClick?.let {
            binding.introSlideNext.setOnClickListener { it() }
        }
        introUiModel.onPreviousClick?.let {
            binding.introSlidePrevious.setOnClickListener { it() }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initSlidesImageView() {
        binding.introSlidesImage.setImageDrawable(
            resources.getDrawable(introUiModel.imageView!!, null)
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun previousAndNextInit(introUiModel: IntroUiModel) {
        binding.apply {
            introSlidePrevious.visibility =
                if (introUiModel.isFirstPage) View.GONE else View.VISIBLE
            introSlideNext.apply {
                if (introUiModel.isLastPage) {
                    text = " << ورود به مام بیبی"
                    setTextColor(activity?.resources!!.getColor(R.color.appDarkPurple, null))
                } else {
                    text = " << بعدی"
                    setTextColor(activity?.resources!!.getColor(R.color.black, null))
                }
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun circleVisibility(page: Int) {
        activity?.resources?.apply {
            circleImageArray.forEachIndexed { index, element ->
                if (page - 1 == index)
                    element.setImageDrawable(getDrawable(R.drawable.filled_circle, null))
                else
                    element.setImageDrawable(getDrawable(R.drawable.empty_circle, null))
            }
        }
    }

}