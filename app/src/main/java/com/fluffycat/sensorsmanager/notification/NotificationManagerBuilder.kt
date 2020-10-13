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
import androidx.core.app.NotificationManagerCompat
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.activities.MainActivity
import com.fluffycat.sensorsmanager.utils.ifIsEmpty
import com.fluffycat.sensorsmanager.utils.ifNotEmpty

const val NOTIFICATION_MAIN_ID = 1
const val NOTIFICATION_MAIN_CHANNEL_ID = "NOTIFICATION_MAIN_CHANNEL"
const val NOTIFICATION_MAIN_CHANNEL_NAME = "Main notification"

const val NOTIFICATION_SERVICE_ID = 2
const val NOTIFICATION_SERVICE_CHANNEL_ID = "NOTIFICATION_SERVICE_CHANNEL"
const val NOTIFICATION_SERVICE_CHANNEL_NAME = "Service notification"

class NotificationManagerBuilder {

    private fun createMainNotification(context: Context, contentTitle: String,
                                       contentText: String, subText: String): Notification {
        val channelId = NOTIFICATION_MAIN_CHANNEL_ID
        createNotificationChannel(context, NOTIFICATION_MAIN_CHANNEL_ID, NOTIFICATION_MAIN_CHANNEL_NAME)
        val intent = createMainActivityIntent(context)
        return createNotification(context, intent, channelId, contentTitle, contentText, subText)
    }

    private fun createCollectingServiceNotification(context: Context, contentTitle: String,
                                                    contentText: String, subText: String): Notification {
        val channelId = NOTIFICATION_SERVICE_CHANNEL_ID
        createNotificationChannel(context, NOTIFICATION_SERVICE_CHANNEL_ID, NOTIFICATION_SERVICE_CHANNEL_NAME)
        val intent = createMainActivityIntent(context)
        return createNotification(context, intent, channelId, contentTitle, contentText, subText)
    }

    private fun createNotification(context: Context, intent: Intent, channelId: String,
                                   contentTitle: String, contentText: String, subText: String) =
        NotificationCompat.Builder(context, channelId).apply {
            setContentTitle(contentTitle ifIsEmpty "SensorManager application is running")
            contentText ifNotEmpty { setContentText(contentText) }
            subText ifNotEmpty { setSubText(subText) }
            setOngoing(true)
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_microscope))
            setSmallIcon(R.drawable.ic_microscope)
            setChannelId(channelId)
            setContentIntent(PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT))
        }.build()

    private fun createNotificationChannel(context: Context, notificationChannelId: String,
                                          notificationChannelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(notificationChannelId, notificationChannelName, NotificationManager.IMPORTANCE_HIGH)
            context.getSystemService(NotificationManager::class.java).createNotificationChannel(notificationChannel)
        }
    }

    private fun createMainActivityIntent(context: Context) = Intent(context, MainActivity::class.java).apply {
        action = Intent.ACTION_MAIN
        addCategory(Intent.CATEGORY_LAUNCHER)
    }

    fun notifyMainNotification(context: Context, contentTitle: String = "", contentText: String = "",
                               subText: String = "") {
        val mainNotification = createMainNotification(context, contentTitle, contentText, subText)
        NotificationManagerCompat.from(context).notify(NOTIFICATION_MAIN_ID, mainNotification)
    }

    fun cancelMainNotification(context: Context) {
        NotificationManagerCompat.from(context).cancel(NOTIFICATION_MAIN_ID)
    }

    fun getServiceNotification(context: Context, contentTitle: String = "",
                               contentText: String = "", subText: String = "") = createCollectingServiceNotification(context, contentTitle, contentText, subText)

    fun notifyServiceNotification(context: Context, contentTitle: String = "Service is running in background",
                                  contentText: String = "", subText: String = "") {
        val serviceNotification = getServiceNotification(context, contentTitle, contentText, subText)
        NotificationManagerCompat.from(context).notify(NOTIFICATION_SERVICE_ID, serviceNotification)
    }

    fun cancelServiceNotification(context: Context) {
        NotificationManagerCompat.from(context).cancel(NOTIFICATION_SERVICE_ID)
    }
}