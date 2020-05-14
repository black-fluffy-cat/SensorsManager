package com.fluffycat.sensorsmanager.listeners

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.fluffycat.sensorsmanager.sensors.ISensorController
import com.fluffycat.sensorsmanager.utils.tag

abstract class MySensorListener(private val dataCollector: ISensorController) : SensorEventListener {

    override fun onSensorChanged(event: SensorEvent) {
        Log.d(tag, "onSensorChanged")
        dataCollector.onSensorDataReceived(event)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // empty
    }

    // TODO to interface?
    abstract fun registerListener(sensorManager: SensorManager)
}