package com.fluffycat.sensorsmanager.listeners

import android.hardware.Sensor
import android.hardware.SensorManager
import com.fluffycat.sensorsmanager.sensors.ISensorController

class LightSensorListener(dataCollector: ISensorController) : MySensorListener(dataCollector) {

    override fun registerListener(sensorManager: SensorManager) {
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_GAME)
    }
}