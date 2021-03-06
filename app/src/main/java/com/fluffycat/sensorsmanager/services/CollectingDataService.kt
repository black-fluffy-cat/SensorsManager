package com.fluffycat.sensorsmanager.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.SensorEvent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.fluffycat.sensorsmanager.notification.NOTIFICATION_SERVICE_ID
import com.fluffycat.sensorsmanager.notification.NotificationManagerBuilder
import com.fluffycat.sensorsmanager.sensors.ISensorController
import com.fluffycat.sensorsmanager.sensors.SensorControllerProvider
import com.fluffycat.sensorsmanager.sensors.SensorType
import com.fluffycat.sensorsmanager.sensors.SensorValueCollector
import com.fluffycat.sensorsmanager.utils.tag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class CollectingDataService : Service() {

    private var sensorController: ISensorController? = null
    private val sensorValueCollector: SensorValueCollector by inject()
    private val notificationManagerBuilder: NotificationManagerBuilder by inject()
    private val sensorControllerProvider: SensorControllerProvider by inject()

    private val isWorking = MutableStateFlow(false)

    inner class LocalBinder(val service: CollectingDataService) : Binder()

    override fun onBind(intent: Intent): IBinder {
        Log.d(tag, "onBind")
        return LocalBinder(this)
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(tag, "onCreate")
        startForeground(NOTIFICATION_SERVICE_ID, notificationManagerBuilder.getServiceNotification(this))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(tag, "onStartCommand")
        when (intent?.action) {
            START_COLLECTING_DATA -> onStartCollectingData()
            STOP_COLLECTING_SERVICE -> stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun onStartCollectingData() {
        if (!isWorking.value) {
            val registeredSuccessfully = setupSensorController()
            if (registeredSuccessfully != true) {
                Log.d(tag, "SensorManager is null or failed to register listener, stopping service")
                stopSelf()
            } else {
                isWorking.value = true
            }
        }
    }

    private fun setupSensorController(): Boolean? {
        sensorController = sensorControllerProvider.getSensorController(SensorType.Accelerometer)
        CoroutineScope(Dispatchers.IO).launch {
            sensorController?.observeSensorCurrentData()?.collect { event -> if (event != null) onDataChanged(event) }
        }
        return sensorController?.startReceivingData()
    }

    private fun onDataChanged(event: SensorEvent) {
        val valuesTriple = Triple(event.values[0], event.values[1], event.values[2])
        sensorValueCollector.addValues(valuesTriple)
    }

    override fun onDestroy() {
        Log.d(tag, "onDestroy")
        notificationManagerBuilder.cancelServiceNotification(this)
        sensorController?.stopReceivingData()
        isWorking.value = false
        super.onDestroy()
    }

    companion object : ServiceStarter {
        private const val START_COLLECTING_DATA = "START_COLLECTING_DATA"
        private const val STOP_COLLECTING_SERVICE = "STOP_COLLECTING_SERVICE"

        override fun getServiceClass() = CollectingDataService::class.java

        fun startCollectingData(context: Context) {
            startWithAction(context, START_COLLECTING_DATA)
        }

        @Suppress("SameParameterValue")
        private fun startWithAction(context: Context, intentAction: String) {
            val intent = getServiceIntent(context).apply { action = intentAction }
            start(context, intent)
        }

        fun stop(context: Context) = start(context, STOP_COLLECTING_SERVICE)
    }
}