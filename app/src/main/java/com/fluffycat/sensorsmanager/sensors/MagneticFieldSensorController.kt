package com.fluffycat.sensorsmanager.sensors

import android.hardware.Sensor
import android.hardware.SensorManager

const val MAGNETIC_FIELD_SENSOR_TYPE = Sensor.TYPE_MAGNETIC_FIELD

class MagneticFieldSensorController(sensorManager: SensorManager?) :
    BaseSensorController(sensorManager, MAGNETIC_FIELD_SENSOR_TYPE)