package com.app.mombaby.utils.utilities

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.navigation.NavController
import androidx.viewpager2.widget.ViewPager2
import com.app.mombaby.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout


object ViewUtils {
    fun isEmptyOrNull(vararg views: EditText): Boolean {
        var isEmpty = false
        for (view in views)
            isEmpty = TextUtils.isEmpty(view.text.toString()) || isEmpty
        return isEmpty
    }

    fun EditText.onTextChangeListener(
        onTextChanged: (str: String) -> Unit
    ) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onTextChanged(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    fun ViewPager2.setCurrentItemSmoothly(
        item: Int,
        duration: Long = 300,
        interpolator: TimeInterpolator = AccelerateDecelerateInterpolator(),
        pagePxWidth: Int = width
    ) {
        val pxToDrag: Int = pagePxWidth * (item - currentItem)
        val animator = ValueAnimator.ofInt(0, pxToDrag)
        var previousValue = 0
        animator.addUpdateListener { valueAnimator ->
            val currentValue = valueAnimator.animatedValue as Int
            val currentPxToDrag = (currentValue - previousValue).toFloat()
            fakeDragBy(-currentPxToDrag)
            previousValue = currentValue
        }
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                beginFakeDrag()
            }

            override fun onAnimationEnd(animation: Animator?) {
                endFakeDrag()
            }

            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
        })
        animator.interpolator = interpolator
        animator.duration = duration
        animator.start()
    }

    fun ViewPager2.setLayoutOffset(
        screenPageLimit: Int = 3,
        pageMarginDp: Int = R.dimen.pageMargin,
        offsetDp: Int = R.dimen.offset
    ) {
        clipToPadding = false
        clipChildren = false
        offscreenPageLimit = screenPageLimit
        val pageMarginPx = resources.getDimensionPixelOffset(pageMarginDp)
        val offsetPx = resources.getDimensionPixelOffset(offsetDp)
        setPageTransformer { page, position ->
            val viewPager = page.parent.parent as ViewPager2
            val offset = position * -(2 * offsetPx + pageMarginPx)
            if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL)
                if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL)
                    page.translationX = -offset
                else
                    page.translationX = offset
            else
                page.translationY = offset
        }
    }

    fun Context.showInternetWarningToast() {
        Toast.makeText(
            this,
            "خطا در ارتباط با اینترنت",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun View.showInternetWarningToast(msg: String = "خطا در ارتباط با اینترنت") {
        this.makeSnackBar(msg)
    }

    @SuppressLint("ResourceType")
    fun View.makeSnackBar(message: String, length: Int = Snackbar.LENGTH_LONG) {
        val snackBar = Snackbar.make(this, message, length)
        val layout = snackBar.view as SnackbarLayout
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            layout.setBackgroundColor(this.resources.getColor(R.color.pink400, null))
        }
        val snackText = layout.findViewById<View>(R.id.snackbar_text) as TextView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            snackText.setTextColor(this.resources.getColor(R.color.white, null))
        }
        snackText.typeface = Typeface.createFromAsset(this.resources.assets, "iran_sans.ttf")
        snackBar.show()
    }

    fun View.backToPreviousDestination(navigator: NavController) {
        this.setOnClickListener { navigator.popBackStack() }
    }

}