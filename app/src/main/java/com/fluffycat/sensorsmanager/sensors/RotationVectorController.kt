package com.fluffycat.sensorsmanager.sensors

import android.hardware.Sensor
import android.hardware.SensorManager

const val ROTATION_VECTOR_SENSOR_TYPE = Sensor.TYPE_ROTATION_VECTOR

class RotationVectorController(sensorManager: SensorManager?) :
    BaseSensorController(sensorManager, ROTATION_VECTOR_SENSOR_TYPE)