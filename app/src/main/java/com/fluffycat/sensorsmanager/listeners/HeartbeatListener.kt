package com.fluffycat.sensorsmanager.listeners

import android.hardware.Sensor
import com.fluffycat.sensorsmanager.sensors.HeartbeatSensorController

// TODO Change argument to interface type :(
class HeartbeatListener(private val heartbeatDataCollector: HeartbeatSensorController) :
    UniversalSensorListener(heartbeatDataCollector) {

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        heartbeatDataCollector.onAdditionalDataChanged(accuracy)
    }
}