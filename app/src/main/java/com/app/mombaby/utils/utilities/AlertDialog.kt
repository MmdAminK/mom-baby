package com.app.mombaby.utils.utilities

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.app.mombaby.R
import com.app.mombaby.databinding.RemoveDialogBinding

class AppAlertDialog(val context: Context) {

    private val dialog: AlertDialog
    private val dialogBinding: RemoveDialogBinding

    init {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        dialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.remove_dialog, null, false
        )
        builder.setView(dialogBinding.root)

        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun setYesButtonAction(slot: () -> Unit) {
        dialogBinding.btnYes.setOnClickListener {
            slot()
            dialog.cancel()
        }
    }

    fun setNoButtonAction(slot: () -> Unit) {
        dialogBinding.btnNo.setOnClickListener {
            slot()
            dialog.cancel()
        }
    }

    fun showDialog() {
        dialog.show()
    }

    fun setMessage(message: String) {
        dialogBinding.message.text = message
    }
}