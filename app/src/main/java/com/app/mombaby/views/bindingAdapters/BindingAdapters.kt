package com.app.mombaby.views.bindingAdapters

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.app.mombaby.R
import com.app.mombaby.utils.animations.AnimUtils.animatedVisibility
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


object BindingAdapters {

    @BindingAdapter("answerSelected")
    @JvmStatic
    fun answerSelected(view: View, isSelected: Boolean) {
        view.animatedVisibility({
            if (isSelected)
                view.setBackgroundColor(Color.parseColor("#FAC4F2"))
            else
                view.setBackgroundColor(Color.parseColor("#ffffff"))
        }, 180)
    }

    @BindingAdapter("loadImage")
    @JvmStatic
    fun loadImage(view: ImageView, url: String) {
        val req =
            RequestOptions().placeholder(R.drawable.no_image_available).error(R.drawable.error)
        Glide.with(view.context).setDefaultRequestOptions(req).load(url).into(view)
    }

}