package com.app.mombaby.utils.utilities


import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.app.mombaby.R
import com.app.mombaby.utils.utilities.alarm.AlarmReceiver


class NotificationHelper(base: Context?, val title: String, val id: Int) : ContextWrapper(base) {
    private var mManager: NotificationManager? = null

    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel =
            NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
        manager!!.createNotificationChannel(channel)
    }

    val manager: NotificationManager?
        get() {
            if (mManager == null) {
                mManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            }
            return mManager
        }
    private val stopIntent = Intent(base, AlarmReceiver::class.java).apply {
        action = "stopAlarm"
    }
    val stopPendingIntent: PendingIntent =
        PendingIntent.getBroadcast(base, id, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    val channelNotification: NotificationCompat.Builder
        get() = NotificationCompat.Builder(applicationContext, channelID)
            .setContentTitle("یادآور مام بیبی")
            .setContentText(title)
            .setSmallIcon(R.drawable.app_icon)
            .addAction(R.drawable.remove_icon, "توقف", stopPendingIntent)
            .setDefaults(0)
            .setOngoing(true)

    companion object {
        const val channelID: String = "channelID"
        const val channelName: String = "Channel Name"
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }
}