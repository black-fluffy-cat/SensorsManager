package com.fluffycat.sensorsmanager.sensors

import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.os.Build
import android.util.Log
import com.fluffycat.sensorsmanager.listeners.UniversalSensorListener
import com.fluffycat.sensorsmanager.utils.BufferedMutableSharedFlow
import com.fluffycat.sensorsmanager.utils.tag

class SensorController(private val sensorManager: SensorManager?, private val sensorType: SensorType) :
    ISensorController {

    override val sensorCurrentData = BufferedMutableSharedFlow<SensorEvent?>()
    override val additionalData = BufferedMutableSharedFlow<Int?>()

    private val sensorListener: UniversalSensorListener by lazy { UniversalSensorListener(this) }

    override fun startReceivingData(): Boolean {
        if (sensorManager == null) {
            Log.e(tag, "sensorManager is null")
            return false
        }

        val sensor = sensorManager.getDefaultSensor(sensorType.type)
        val registerStatus = sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_GAME)
        if (registerStatus) {
            Log.d(tag, "Started receiving data")
        } else {
            Log.e(tag, "Registering sensor failed, sensorManager: $sensorManager, registerStatus: $registerStatus")
        }
        return registerStatus
    }

    override fun stopReceivingData() {
        sensorManager?.unregisterListener(sensorListener)
        Log.d(tag, "Stopped receiving data")
    }

    override fun onSensorDataReceived(event: SensorEvent) {
        sensorCurrentData.tryEmit(event)
    }

    override fun onAdditionalDataChanged(accuracy: Int) {
        additionalData.tryEmit(accuracy)
    }

    override fun getSensorInfo(): String {
        val sensor = sensorManager?.getDefaultSensor(sensorType.type) ?: return ""
        return buildString {
            append("${sensor.name}\n")
            append("Maximum range: ${sensor.maximumRange}\n")
            append("Power: ${sensor.power}\n")
            append("Resolution: ${sensor.resolution}\n")
            append("Type: ${sensor.type}\n")
            append("Vendor: ${sensor.vendor}\n")
            append("Version: ${sensor.version}\n")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                append("Id: ${sensor.id}\n")
            }
        }
    }
}