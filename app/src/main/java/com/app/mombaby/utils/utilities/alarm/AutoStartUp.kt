package com.app.mombaby.utils.utilities.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.app.mombaby.utils.utilities.NotificationHelper

class AutoStartUp : BroadcastReceiver() {

    companion object {
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        var ringtone: Ringtone? = null
        lateinit var notificationHelper: NotificationHelper
    }


    override fun onReceive(context: Context, intent: Intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {


            val action = intent.action

            if (ringtone == null)
                ringtone = RingtoneManager.getRingtone(context, notification)

            action?.let {
                if (action == "stopAlarm") {
                    ringtone?.stop()
                    notificationHelper.manager?.cancelAll()
                }
            } ?: run {
                val title = intent.getStringExtra("title")
                val id = intent.getIntExtra("id", 1)
                notificationHelper = NotificationHelper(context, title!!, id)
                val nb: NotificationCompat.Builder = notificationHelper.channelNotification
                notificationHelper.manager?.notify(id, nb.build())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) ringtone!!.isLooping = true
                ringtone?.play()
            }
        }

    }
}