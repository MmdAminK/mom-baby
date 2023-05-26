package com.app.mombaby.ui.fragments.mainPage.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.mombaby.R
import com.app.mombaby.databinding.BlogItemDetailBinding
import com.app.mombaby.models.pregnancyHome.PregnancyBlog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BlogItemDetail : Fragment() {

    lateinit var binding: BlogItemDetailBinding

    lateinit var blog: PregnancyBlog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        blog = requireArguments().get("blog") as PregnancyBlog

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
        binding = DataBindingUtil.inflate(inflater, R.layout.blog_item_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pregnancyBlog = blog

        binding.back.setOnClickListener { findNavController().popBackStack() }
    }

}