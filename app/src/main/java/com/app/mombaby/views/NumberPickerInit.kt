package com.app.mombaby.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.RequiresApi
import com.app.mombaby.R
import com.shawnlin.numberpicker.NumberPicker

@RequiresApi(Build.VERSION_CODES.M)
@SuppressLint("UseCompatLoadingForDrawables", "WrongConstant")
class MomBabyNumberPicker : NumberPicker {
    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
        attrs,
        defStyleAttr
    )

    init {
        this.setSelectedTypeface(Typeface.createFromAsset(resources.assets, "iran_sans_bold.ttf"))
        this.typeface = Typeface.createFromAsset(resources.assets, "iran_sans.ttf")
        this.textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            16f,
            context?.resources?.displayMetrics
        )
        this.selectedTextSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            20f,
            context?.resources?.displayMetrics
        )
        this.background = context?.resources?.getDrawable(R.drawable.fade_gradiant, null)
        this.selectedTextColor = context?.resources?.getColor(R.color.appDarkPurple, null)!!
        this.textColor = context?.resources?.getColor(R.color.black, null)!!
        this.wrapSelectorWheel = false
        this.wheelItemCount = 5
        this.value = (this.maxValue + this.minValue) / 2
        this.orientation = 0
    }

}