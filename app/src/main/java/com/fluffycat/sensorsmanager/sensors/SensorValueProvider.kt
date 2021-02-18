package com.fluffycat.sensorsmanager.sensors

import android.hardware.Sensor
import android.os.Build
import com.fluffycat.sensorsmanager.R

const val ACCELEROMETER_SENSOR_TYPE = Sensor.TYPE_ACCELEROMETER
const val GYROSCOPE_SENSOR_TYPE = Sensor.TYPE_GYROSCOPE
const val LIGHT_SENSOR_TYPE = Sensor.TYPE_LIGHT
const val LINEAR_ACCELERATION_SENSOR_TYPE = Sensor.TYPE_LINEAR_ACCELERATION
const val MAGNETIC_FIELD_SENSOR_TYPE = Sensor.TYPE_MAGNETIC_FIELD
const val PROXIMITY_SENSOR_TYPE = Sensor.TYPE_PROXIMITY
const val ROTATION_VECTOR_SENSOR_TYPE = Sensor.TYPE_ROTATION_VECTOR
val HEART_RATE_SENSOR_TYPE = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
    Sensor.TYPE_HEART_RATE
} else {
    -1
}

class SensorValueProvider {
    fun getExistingSensors() =
        listOf(ACCELEROMETER_SENSOR_TYPE, GYROSCOPE_SENSOR_TYPE, LIGHT_SENSOR_TYPE, LINEAR_ACCELERATION_SENSOR_TYPE,
                MAGNETIC_FIELD_SENSOR_TYPE, PROXIMITY_SENSOR_TYPE, ROTATION_VECTOR_SENSOR_TYPE, HEART_RATE_SENSOR_TYPE)

    private val menuItemsAndCorrespondingSensors: Map<Int, Int> = listOf(
            R.id.accelerometerMenuItem to ACCELEROMETER_SENSOR_TYPE,
            R.id.gyroscopeMenuItem to GYROSCOPE_SENSOR_TYPE,
            R.id.heartbeatSensorMenuItem to HEART_RATE_SENSOR_TYPE,
            R.id.linearAccelerationSensorMenuItem to LINEAR_ACCELERATION_SENSOR_TYPE,
            R.id.lightSensorMenuItem to LIGHT_SENSOR_TYPE,
            R.id.magneticFieldSensorMenuItem to MAGNETIC_FIELD_SENSOR_TYPE,
            R.id.rotationVectorMenuItem to ROTATION_VECTOR_SENSOR_TYPE,
            R.id.proximitySensorMenuItem to PROXIMITY_SENSOR_TYPE).toMap()

    fun getMenuItemsAndCorrespondingSensors() = menuItemsAndCorrespondingSensors
}