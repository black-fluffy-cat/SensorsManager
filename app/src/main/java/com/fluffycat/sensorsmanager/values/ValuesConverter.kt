package com.fluffycat.sensorsmanager.values

import android.hardware.Sensor
import com.fluffycat.sensorsmanager.preferences.PreferencesManager
import com.fluffycat.sensorsmanager.sensors.SensorType

// All sensors return Float type values. The numeric conversions and rounding live in the
// shared Kotlin Multiplatform module; this class maps Android sensor types and the user's
// stored unit preferences onto that shared logic.
class ValuesConverter(private val preferencesManager: PreferencesManager) {

    private val converter = MeasurementConverter()

    fun convertValueToChosenUnit(value: Float, sensor: Sensor?): Float = when (sensor?.type) {
        SensorType.Accelerometer.type -> convertDistanceValueToChosenUnit(value)
        SensorType.Gyroscope.type -> convertAngleValueToChosenUnit(value)
        SensorType.Light.type -> value
        SensorType.LinearAcceleration.type -> convertDistanceValueToChosenUnit(value)
        SensorType.MagneticField.type -> value
        SensorType.Proximity.type -> value
        SensorType.HeartRate.type -> value
        SensorType.RotationVector.type -> value
        else -> value
    }

    fun convertValueToStringWithSymbol(value: Float, sensor: Sensor?): String = when (sensor?.type) {
        SensorType.Accelerometer.type -> convertAccelerationValueToStringWithSymbol(value)
        SensorType.Gyroscope.type -> convertAngleValueToStringWithSymbol(value)
        SensorType.Light.type -> "Lux: $value"
        SensorType.LinearAcceleration.type -> convertDistanceValueToStringWithSymbol(value)
        SensorType.MagneticField.type -> convertMagneticFieldValueToStringWithSymbol(value)
        SensorType.Proximity.type -> "$value cm"
        SensorType.HeartRate.type -> value.toString()
        SensorType.RotationVector.type -> value.toString()
        else -> value.toString()
    }

    fun roundValue(value: Float, decimalPlaces: Int = 3): Float = converter.roundValue(value, decimalPlaces)

    fun convertAngularVelocityValueToStringWithSymbol(angularVelocityInRadians: Float): String =
        convertAngleValueToStringWithSymbol(angularVelocityInRadians) + "/s"

    fun convertTemperatureValueToStringWithSymbol(temperatureInCelsius: Float): String {
        val unit = preferencesManager.readChosenTemperatureUnit()
        val convertedTemperatureValue = converter.convertTemperature(temperatureInCelsius, unit)
        return "$convertedTemperatureValue ${converter.temperatureSymbol(unit)}"
    }

    private fun convertMagneticFieldValueToStringWithSymbol(magneticFieldInMicroTesla: Float) =
        "$magneticFieldInMicroTesla ${converter.magneticFieldSymbol()}"

    private fun convertAngleValueToStringWithSymbol(angleInRadians: Float): String {
        val unit = preferencesManager.readChosenAngleUnit()
        val convertedAngleValue = converter.convertAngle(angleInRadians, unit)
        return "$convertedAngleValue ${converter.angleSymbol(unit)}"
    }

    private fun convertAngleValueToChosenUnit(angleInRadians: Float): Float =
        converter.convertAngle(angleInRadians, preferencesManager.readChosenAngleUnit())

    private fun convertDistanceValueToStringWithSymbol(distanceInMeters: Float): String {
        val unit = preferencesManager.readChosenDistanceUnit()
        val convertedDistanceValue = converter.convertDistance(distanceInMeters, unit)
        return "$convertedDistanceValue ${converter.distanceSymbol(unit)}"
    }

    private fun convertAccelerationValueToStringWithSymbol(accelerationInMeters: Float): String =
        convertDistanceValueToStringWithSymbol(accelerationInMeters) + "/s²"

    private fun convertDistanceValueToChosenUnit(distanceInMeters: Float): Float =
        converter.convertDistance(distanceInMeters, preferencesManager.readChosenDistanceUnit())
}
