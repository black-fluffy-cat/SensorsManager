package com.fluffycat.sensorsmanager.sensors

import android.hardware.Sensor
import android.hardware.SensorManager

const val GYROSCOPE_SENSOR_TYPE = Sensor.TYPE_GYROSCOPE

class GyroscopeSensorController(sensorManager: SensorManager?) :
    BaseSensorController(sensorManager, GYROSCOPE_SENSOR_TYPE)