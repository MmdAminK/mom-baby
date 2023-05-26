package com.app.mombaby.utils.animations

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.view.View

class PulseAnimation(
    val view: View,
    var scale: Float = 1.25f,
    var duration: Long = 350,
    var repeatCount: Int = ObjectAnimator.INFINITE,
    var repeatMode: Int = ObjectAnimator.REVERSE) {

    @SuppressLint("Recycle")
    fun start() {
        val pulseAnimation: ValueAnimator = ObjectAnimator.ofPropertyValuesHolder(
            view,
            PropertyValuesHolder.ofFloat("scaleX", scale),
            PropertyValuesHolder.ofFloat("scaleY", scale)
        )
        pulseAnimation.duration = duration
        pulseAnimation.repeatCount = repeatCount
        pulseAnimation.repeatMode = repeatMode

        pulseAnimation.start()
    }
}