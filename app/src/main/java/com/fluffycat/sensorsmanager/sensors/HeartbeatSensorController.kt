package com.fluffycat.sensorsmanager.sensors

import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import androidx.lifecycle.MutableLiveData
import com.fluffycat.sensorsmanager.listeners.HeartbeatListener
import com.fluffycat.sensorsmanager.listeners.UniversalSensorListener

val HEART_RATE_SENSOR_TYPE = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
    Sensor.TYPE_HEART_RATE
} else {
    -1
}

class HeartbeatSensorController(sensorManager: SensorManager) :
    SensorController(sensorManager, HEART_RATE_SENSOR_TYPE) {

    val additionalData = MutableLiveData<Int>()

    override val sensorListener: UniversalSensorListener = HeartbeatListener(this)

    fun onAdditionalDataChanged(code: Int) {
        additionalData.value = code
    }
}