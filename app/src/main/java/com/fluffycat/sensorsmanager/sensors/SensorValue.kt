package com.fluffycat.sensorsmanager.sensors

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