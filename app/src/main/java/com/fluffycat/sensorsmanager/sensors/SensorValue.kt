package com.fluffycat.sensorsmanager.sensors

import android.hardware.Sensor

enum class UsedSensors {
    ACCELEROMETER,
    GYROSCOPE,
    HEARTBEAT,
    LIGHT,
    LINEAR_ACCELERATION,
    MAGNETIC_FIELD,
    PROXIMITY,
    ROTATION_VECTOR
}

data class SensorValue(val value: Float, val sensor: UsedSensors)

const val ACCELEROMETER_SENSOR_TYPE = Sensor.TYPE_ACCELEROMETER
const val GYROSCOPE_SENSOR_TYPE = Sensor.TYPE_GYROSCOPE
const val LIGHT_SENSOR_TYPE = Sensor.TYPE_LIGHT
const val LINEAR_ACCELERATION_SENSOR_TYPE = Sensor.TYPE_LINEAR_ACCELERATION
const val MAGNETIC_FIELD_SENSOR_TYPE = Sensor.TYPE_MAGNETIC_FIELD
const val PROXIMITY_SENSOR_TYPE = Sensor.TYPE_PROXIMITY
const val ROTATION_VECTOR_SENSOR_TYPE = Sensor.TYPE_ROTATION_VECTOR