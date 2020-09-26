package com.fluffycat.sensorsmanager.sensors

import android.hardware.Sensor
import android.hardware.SensorManager

const val LIGHT_SENSOR_TYPE = Sensor.TYPE_LIGHT

class LightSensorController(sensorManager: SensorManager?) : BaseSensorController(sensorManager, LIGHT_SENSOR_TYPE)