package com.app.mombaby.utils.utilities

import android.app.Activity
import android.graphics.Typeface
import android.view.View
import com.app.mombaby.R
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView


object TapTargetBuilder {
    fun Activity.createTapTarget(
        id: Int,
        view: View,
        title: String,
        description: String
    ): TapTarget {
        return TapTarget.forView(view, title, description)
            .outerCircleColor(R.color.appDarkPurple)
            .outerCircleAlpha(0.96f)
            .targetCircleColor(R.color.white)
            .titleTextSize(20)
            .titleTextColor(R.color.white)
            .descriptionTextSize(16)
            .descriptionTextColor(R.color.white)
            .textColor(R.color.white)
            .textTypeface(Typeface.SANS_SERIF)
            .dimColor(R.color.black)
            .drawShadow(true)
            .cancelable(false)
            .tintTarget(true)
            .transparentTarget(false)
            .targetRadius(55)
            .textTypeface(Typeface.createFromAsset(assets, "iran_sans.ttf"))
            .id(id)
    }

    fun Activity.createTapTarget(
        id: Int,
        view: View,
        title: String,
        description: String,
        onTargetClick: () -> Unit
    ): TapTarget {
        val tapTarget = TapTarget.forView(view, title, description)
            .outerCircleColor(R.color.appDarkPurple)
            .outerCircleAlpha(0.96f)
            .targetCircleColor(R.color.white)
            .titleTextSize(20)
            .titleTextColor(R.color.white)
            .descriptionTextSize(16)
            .descriptionTextColor(R.color.white)
            .textColor(R.color.white)
            .textTypeface(Typeface.SANS_SERIF)
            .dimColor(R.color.black)
            .drawShadow(true)
            .cancelable(false)
            .tintTarget(true)
            .transparentTarget(false)
            .targetRadius(55)
            .textTypeface(Typeface.createFromAsset(assets, "iran_sans.ttf"))
            .id(id)
        object : TapTargetView.Listener() {
            override fun onTargetClick(view: TapTargetView?) {
                super.onTargetClick(view)
                onTargetClick()
            }
        }
        TapTargetView.showFor(this, tapTarget)
        return tapTarget
    }
}
