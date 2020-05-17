package com.fluffycat.sensorsmanager.listeners

import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.fluffycat.sensorsmanager.sensors.HeartbeatSensorController

// TODO Change argument to interface type :(
class HeartbeatListener(private val heartbeatDataCollector: HeartbeatSensorController) :
    MySensorListener(heartbeatDataCollector) {

    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun registerListener(sensorManager: SensorManager) {
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE), SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        heartbeatDataCollector.onAdditionalDataChanged(accuracy)
    }
}