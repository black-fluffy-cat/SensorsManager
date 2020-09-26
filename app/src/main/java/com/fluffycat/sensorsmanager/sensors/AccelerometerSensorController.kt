package com.fluffycat.sensorsmanager.sensors

import android.hardware.Sensor
import android.hardware.SensorManager

const val ACCELEROMETER_SENSOR_TYPE = Sensor.TYPE_ACCELEROMETER

class AccelerometerSensorController(sensorManager: SensorManager?) :
    BaseSensorController(sensorManager, ACCELEROMETER_SENSOR_TYPE)