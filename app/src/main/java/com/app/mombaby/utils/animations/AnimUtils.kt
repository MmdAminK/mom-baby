package com.app.mombaby.utils.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.View

object AnimUtils {

    fun View.animatedVisibility(func: () -> Unit, duration: Long = 300) {
        val alphaAnimator: ObjectAnimator =
            ObjectAnimator.ofFloat(this, View.ALPHA, 0.0f, 1.0f)
        alphaAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                func()
            }
        })
        alphaAnimator.duration = duration
        alphaAnimator.start()
    }
}