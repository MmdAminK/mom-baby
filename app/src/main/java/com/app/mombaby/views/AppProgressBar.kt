package com.app.mombaby.views

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.ImageView
import com.app.mombaby.R
import com.app.mombaby.utils.animations.PulseAnimation

// ToDo : convert this class to object

class AppProgressBar private constructor(context: Context) {

    private var mDialog: Dialog? = null

    init {
        mDialog = Dialog(context)
        mDialog?.let { dialog ->
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.app_progress_bar)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val imageView: ImageView = dialog.findViewById(R.id.progress_image)
            PulseAnimation(imageView, scale = 0.8f).start()
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
        }
    }

    fun hide() {
        mDialog!!.dismiss()
    }

    fun show() {
        mDialog!!.show()
    }

    fun cancellable(cancellable: Boolean) {
        mDialog!!.setCancelable(cancellable)
    }

    companion object {
        @Volatile
        private lateinit var appProgressBar: AppProgressBar

        fun instance(context: Context): AppProgressBar {
            synchronized(this) {
                if (!Companion::appProgressBar.isInitialized)
                    appProgressBar = AppProgressBar(context)
            }
            return appProgressBar
        }
    }
}