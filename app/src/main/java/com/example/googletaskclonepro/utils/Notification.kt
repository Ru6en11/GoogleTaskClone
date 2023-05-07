package com.example.googletaskclonepro.utils

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.googletaskclonepro.R

const val notificationId = 1
const val chanelId = "chanel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"

class Notification : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notification : android.app.Notification = NotificationCompat.Builder(context, chanelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(messageExtra))
            .build()

        val manager = context.getSystemService((Context.NOTIFICATION_SERVICE)) as NotificationManager
        manager.notify(notificationId, notification)
    }
}