package com.fluffycat.sensorsmanager.sensors

import android.hardware.Sensor
import android.os.Build
import android.os.Parcelable
import com.fluffycat.sensorsmanager.R
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class SensorType(val type: Int) : Parcelable {
    Accelerometer(Sensor.TYPE_ACCELEROMETER),
    Gyroscope(Sensor.TYPE_GYROSCOPE),
    Light(Sensor.TYPE_LIGHT),
    LinearAcceleration(Sensor.TYPE_LINEAR_ACCELERATION),
    MagneticField(Sensor.TYPE_MAGNETIC_FIELD),
    Proximity(Sensor.TYPE_PROXIMITY),
    RotationVector(Sensor.TYPE_ROTATION_VECTOR),
    HeartRate(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
        Sensor.TYPE_HEART_RATE
    } else {
        -1
    })
}

class SensorTypeProvider {
    fun getExistingSensors() = SensorType::class.sealedSubclasses

    private val menuItemsAndCorrespondingSensors: Map<Int, SensorType> = listOf(
            R.id.accelerometerMenuItem to SensorType.Accelerometer,
            R.id.gyroscopeMenuItem to SensorType.Gyroscope,
            R.id.heartbeatSensorMenuItem to SensorType.HeartRate,
            R.id.linearAccelerationSensorMenuItem to SensorType.LinearAcceleration,
            R.id.lightSensorMenuItem to SensorType.Light,
            R.id.magneticFieldSensorMenuItem to SensorType.MagneticField,
            R.id.rotationVectorMenuItem to SensorType.RotationVector,
            R.id.proximitySensorMenuItem to SensorType.Proximity).toMap()

    fun getMenuItemsAndCorrespondingSensors() = menuItemsAndCorrespondingSensors
}