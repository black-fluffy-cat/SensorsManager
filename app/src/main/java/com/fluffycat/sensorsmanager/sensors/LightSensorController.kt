package com.fluffycat.sensorsmanager.sensors

import android.content.Context
import android.hardware.Sensor

private const val LIGHT_SENSOR_TYPE = Sensor.TYPE_LIGHT

class LightSensorController(context: Context) : BaseSensorController(context, LIGHT_SENSOR_TYPE)