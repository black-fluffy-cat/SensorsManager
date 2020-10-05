package com.fluffycat.sensorsmanager.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.activities.MainActivity

const val NOTIFICATION_MAIN_CHANNEL_ID = "NOTIFICATION_MAIN_CHANNEL"
const val NOTIFICATION_MAIN_CHANNEL_NAME = "Main notification"

class NotificationManagerBuilder {

    fun createMainNotification(context: Context): Notification {
        val channelId = NOTIFICATION_MAIN_CHANNEL_ID

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(NOTIFICATION_MAIN_CHANNEL_ID, NOTIFICATION_MAIN_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH)
            context.getSystemService(NotificationManager::class.java).createNotificationChannel(notificationChannel)
        }

        val intent = createMainActivityIntent(context)

        return NotificationCompat.Builder(context, channelId).apply {
            setContentTitle("Notification title")
            setOngoing(true)
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_microscope))
            setSmallIcon(R.drawable.ic_microscope)
            setChannelId(channelId)
            setContentIntent(PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT))
        }.build()
    }

    private fun createMainActivityIntent(context: Context) = Intent(context, MainActivity::class.java).apply {
        action = Intent.ACTION_MAIN
        addCategory(Intent.CATEGORY_LAUNCHER)
    }
}