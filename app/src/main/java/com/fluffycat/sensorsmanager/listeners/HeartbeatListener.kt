package com.fluffycat.sensorsmanager.listeners

import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.fluffycat.sensorsmanager.sensors.ISensorController

class HeartbeatListener(dataCollector: ISensorController) : MySensorListener(dataCollector) {

    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun registerListener(sensorManager: SensorManager) {
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE), SensorManager.SENSOR_DELAY_GAME)
    }
}