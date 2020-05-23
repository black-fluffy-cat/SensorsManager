package com.fluffycat.sensorsmanager.sensors

import android.content.Context
import android.hardware.Sensor

private const val MAGNETIC_FIELD_SENSOR_TYPE = Sensor.TYPE_MAGNETIC_FIELD

class MagneticFieldSensorController(context: Context) : BaseSensorController(context, MAGNETIC_FIELD_SENSOR_TYPE)