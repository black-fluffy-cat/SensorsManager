package com.fluffycat.sensorsmanager.sensors

import android.content.Context
import android.hardware.Sensor

private const val ACCELEROMETER_SENSOR_TYPE = Sensor.TYPE_ACCELEROMETER

class AccelerometerSensorController(context: Context) : BaseSensorController(context, ACCELEROMETER_SENSOR_TYPE)