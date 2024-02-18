package com.example.gratefulnote.daily_notification.data.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.gratefulnote.MainActivity
import com.example.gratefulnote.R

const val OPEN_APP_INTENT_ID = 1
const val NOTIFICATION_ID = 2

fun NotificationManager.sendNotification(context : Context){
    val openAppIntent = Intent(context , MainActivity::class.java)

    val openAppPendingIntent = PendingIntent.getActivity(context ,
            OPEN_APP_INTENT_ID ,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

    val builder = NotificationCompat.Builder(
        context,
        context.getString(R.string.notification_channel_id)
    )
        .setContentText(context.getString(R.string.notification_content_text))
        .setContentIntent(openAppPendingIntent)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setSmallIcon(R.drawable.ic_check)

    notify(NOTIFICATION_ID , builder.build())
}