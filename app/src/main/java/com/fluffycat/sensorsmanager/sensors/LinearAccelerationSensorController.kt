package com.fluffycat.sensorsmanager.sensors

import android.hardware.Sensor
import android.hardware.SensorManager

const val LINEAR_ACCELERATION_SENSOR_TYPE = Sensor.TYPE_LINEAR_ACCELERATION

class LinearAccelerationSensorController(sensorManager: SensorManager?) :
    BaseSensorController(sensorManager, LINEAR_ACCELERATION_SENSOR_TYPE)