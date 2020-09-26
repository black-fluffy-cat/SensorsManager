package com.fluffycat.sensorsmanager.sensors

import android.hardware.Sensor
import android.hardware.SensorManager

const val PROXIMITY_SENSOR_TYPE = Sensor.TYPE_PROXIMITY

class ProximitySensorController(sensorManager: SensorManager?) :
    BaseSensorController(sensorManager, PROXIMITY_SENSOR_TYPE)