package com.example.goalkeeper.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationManagerCompat

fun createNotificationChannel(context: Context, channelId: String) {
    val name = "goalKeeper_notification_channel"
    val descriptionText = "GoalKeeper Notification Channel"
    val importance = NotificationManager.IMPORTANCE_HIGH
    val channel = NotificationChannel(channelId, name, importance).apply {
        description = descriptionText
    }

    with(NotificationManagerCompat.from(context)) {
        createNotificationChannel(channel)
    }
}