package com.fluffycat.sensorsmanager.services

import android.content.Context
import android.content.Intent
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LifecycleService
import com.fluffycat.sensorsmanager.notification.NotificationManagerBuilder
import com.fluffycat.sensorsmanager.sensors.ACCELEROMETER_SENSOR_TYPE
import com.fluffycat.sensorsmanager.sensors.ISensorController
import com.fluffycat.sensorsmanager.sensors.SensorController
import com.fluffycat.sensorsmanager.utils.BufferedMutableSharedFlow
import com.fluffycat.sensorsmanager.utils.tag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class CollectingDataService : LifecycleService() {

    // TODO try to mock it
    private val notificationManagerBuilder = NotificationManagerBuilder()

    private var sensorController: ISensorController? = null

    private val eventValues = BufferedMutableSharedFlow<Triple<Float, Float, Float>?>()

    fun observeEventValues(): SharedFlow<Triple<Float, Float, Float>?> = eventValues

    inner class LocalBinder(val service: CollectingDataService) : Binder()

    override fun onBind(intent: Intent): IBinder {
        Log.d(tag, "onBind")
        super.onBind(intent)
        return LocalBinder(this)
    }

    override fun onCreate() {
        isRunning.set(true)
        super.onCreate()
        Log.d(tag, "onCreate")
        (getSystemService(Context.SENSOR_SERVICE) as SensorManager?)?.let { sensorManager ->
            setupSensorController(sensorManager)
        } ?: run {
            Log.d(tag, "SensorManager is null, stopping service")
            stopSelf()
        }
    }

    private fun setupSensorController(manager: SensorManager): Boolean? {
        sensorController = SensorController(manager, ACCELEROMETER_SENSOR_TYPE)
        CoroutineScope(Dispatchers.IO).launch {
            sensorController?.observeSensorCurrentData()?.collect { event -> if (event != null) onDataChanged(event) }
        }
        return sensorController?.startReceivingData()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(tag, "onStartCommand")
        notificationManagerBuilder.notifyServiceNotification(this)
//        startForeground(NOTIFICATION_SERVICE_ID, notificationManagerBuilder.getServiceNotification(this))
        return super.onStartCommand(intent, flags, startId)
    }

    private fun onDataChanged(event: SensorEvent) {
//        Log.d(tag, "onDataChanged, values: ${event.values[0]}, ${event.values[1]}, ${event.values[2]}")
        eventValues.tryEmit(Triple(event.values[0], event.values[1], event.values[2]))
    }

    override fun onDestroy() {
        Log.d(tag, "onDestroy")
        notificationManagerBuilder.cancelServiceNotification(this)
        sensorController?.stopReceivingData()
        isRunning.set(false)
        super.onDestroy()
    }

    companion object {
        fun start(context: Context) {
            Log.d(CollectingDataService::class.simpleName, "startService")
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                context.startForegroundService(getServiceIntent(context))
//            } else {
            context.startService(getServiceIntent(context))
//            }
        }

        fun stop(context: Context) {
            Log.d(CollectingDataService::class.simpleName, "stopService")
            context.stopService(getServiceIntent(context))
        }

        fun getServiceIntent(context: Context) = Intent(context, CollectingDataService::class.java)

        val isRunning = AtomicBoolean(false)
    }
}