package com.app.mombaby.utils.utilities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController

object CommonUtils {

    fun Activity.openLinkInBrowser(link: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(browserIntent)
    }

    fun FragmentActivity.onBackPressed(callBackAction: () -> Unit) {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    callBackAction()
                }
            }
        this.onBackPressedDispatcher.addCallback(this, callback)
    }

    fun FragmentActivity.backToPreviousDestination(navigator: NavController) {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigator.popBackStack()
                }
            }
        this.onBackPressedDispatcher.addCallback(this, callback)
    }
}