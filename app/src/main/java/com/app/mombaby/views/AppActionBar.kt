package com.app.mombaby.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import com.app.mombaby.R
import com.app.mombaby.databinding.AppActionBarBinding


class AppActionBar : CardView {

    private var binding: AppActionBarBinding =
        AppActionBarBinding.inflate(LayoutInflater.from(context), this, true)

    var title: String = ""

    constructor(context: Context?) : super(context!!)

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.AppActionBar,
            0, 0
        )
        if (typedArray.getString(R.styleable.AppActionBar_title).toString() != "null")
            title = typedArray.getString(R.styleable.AppActionBar_title).toString()

        binding.appActionBarTitle.text = title
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
        attrs,
        defStyleAttr
    ) {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.AppActionBar,
            0, 0
        )
        title = typedArray.getString(R.styleable.AppActionBar_title).toString()
        binding.appActionBarTitle.text = title
    }

    fun onBackClickListener(onBack: () -> Unit) {
        binding.appActionBarBack.setOnClickListener { onBack() }
    }

    @JvmName("setTitle1")
    fun setTitle(title: String) {
        binding.appActionBarTitle.text = title
    }
}