package com.fluffycat.sensorsmanager.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.Observer
import com.fluffycat.sensorsmanager.notification.NotificationManagerBuilder
import com.fluffycat.sensorsmanager.sensors.ACCELEROMETER_SENSOR_TYPE
import com.fluffycat.sensorsmanager.sensors.ISensorController
import com.fluffycat.sensorsmanager.sensors.SensorController
import com.fluffycat.sensorsmanager.utils.tag

class CollectingDataService : LifecycleService() {

    // TODO try to mock it
    private val notificationManagerBuilder = NotificationManagerBuilder()
    private var sensorController: ISensorController? = null
    private var sensorManager: SensorManager? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager?
        sensorManager?.let { manager ->
            sensorController = SensorController(manager, ACCELEROMETER_SENSOR_TYPE)
            sensorController?.sensorCurrentData?.observe(, Observer { sensorEvent ->
                onDataChanged(sensorEvent)
            })
        }
        if (sensorManager == null) {
            Log.d(tag, "SensorManager is null, stopping service")
            stopSelf()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notificationManagerBuilder.notifyServiceNotification(this)
        return START_STICKY
    }

    fun onDataChanged(event: SensorEvent) {
        Log.d(tag, "onDataChanged, values: ${event.values[0]}, ${event.values[1]}, ${event.values[2]}")
    }

    override fun onDestroy() {
        sensorController?.stopReceivingData()
        super.onDestroy()
    }

    companion object {
        fun start(context: Context) {
            context.startService(Intent(context, CollectingDataService::class.java))
        }
    }
}