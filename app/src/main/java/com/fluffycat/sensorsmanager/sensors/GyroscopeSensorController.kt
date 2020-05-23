package com.fluffycat.sensorsmanager.sensors

import android.content.Context
import android.hardware.Sensor

private const val GYROSCOPE_SENSOR_TYPE = Sensor.TYPE_GYROSCOPE

class GyroscopeSensorController(context: Context) : BaseSensorController(context, GYROSCOPE_SENSOR_TYPE)