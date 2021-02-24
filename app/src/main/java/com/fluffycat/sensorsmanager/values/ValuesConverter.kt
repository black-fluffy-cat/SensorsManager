package com.fluffycat.sensorsmanager.values

import android.hardware.Sensor
import com.fluffycat.sensorsmanager.preferences.*
import com.fluffycat.sensorsmanager.sensors.*
import kotlin.math.pow
import kotlin.math.roundToInt

// All sensors return Float type values
class ValuesConverter(private val preferencesManager: PreferencesManager) {


    fun convertValueToChosenUnit(value: Float, sensor: Sensor?): Float = when (sensor?.type) {
        ACCELEROMETER_SENSOR_TYPE -> convertDistanceValueToChosenUnit(value)
        GYROSCOPE_SENSOR_TYPE -> convertAngleValueToChosenUnit(value)
        HEART_RATE_SENSOR_TYPE -> value
        LIGHT_SENSOR_TYPE -> value
        LINEAR_ACCELERATION_SENSOR_TYPE -> convertDistanceValueToChosenUnit(value)
        MAGNETIC_FIELD_SENSOR_TYPE -> value
        PROXIMITY_SENSOR_TYPE -> value
        ROTATION_VECTOR_SENSOR_TYPE -> value
        else -> value
    }

    fun convertValueToStringWithSymbol(value: Float, sensor: Sensor?): String = when (sensor?.type) {
        ACCELEROMETER_SENSOR_TYPE -> convertAccelerationValueToStringWithSymbol(value)
        GYROSCOPE_SENSOR_TYPE -> convertAngleValueToStringWithSymbol(value)
        HEART_RATE_SENSOR_TYPE -> value.toString()
        LIGHT_SENSOR_TYPE -> "Lux: $value"
        LINEAR_ACCELERATION_SENSOR_TYPE -> convertDistanceValueToStringWithSymbol(value)
        MAGNETIC_FIELD_SENSOR_TYPE -> convertMagneticFieldValueToStringWithSymbol(value)
        PROXIMITY_SENSOR_TYPE -> "$value cm"
        ROTATION_VECTOR_SENSOR_TYPE -> value.toString()
        else -> value.toString()
    }


    fun roundValue(value: Float, decimalPlaces: Int = 3): Float {
        if (decimalPlaces < 0 || decimalPlaces > 7) return value
        val decimalHelperValue = 10.0.pow(decimalPlaces)
        return ((value * decimalHelperValue).roundToInt()) / decimalHelperValue.toFloat()
    }

    //  TODO where it was and why it is gone
    fun convertAngularVelocityValueToStringWithSymbol(angularVelocityInDegrees: Float): String =
        convertAngleValueToStringWithSymbol(angularVelocityInDegrees) + "/s"


    fun convertTemperatureValueToStringWithSymbol(temperatureInCelsius: Float): String {
        val convertedTemperatureValue = convertTemperatureValueToChosenUnit(temperatureInCelsius)
        val unitSymbol = getCurrentChosenTemperatureUnitSymbol()
        return "$convertedTemperatureValue $unitSymbol"
    }

    private fun getCurrentChosenAngleUnitSymbol(): String = when (preferencesManager.readChosenAngleUnit()) {
        AngleUnit.DEGREE -> "°"
        AngleUnit.RADIAN -> "rad"
    }

    private fun getCurrentChosenTemperatureUnitSymbol(): String =
        when (preferencesManager.readChosenTemperatureUnit()) {
            TemperatureUnit.CELSIUS -> "°C"
            TemperatureUnit.KELVIN -> "K"
            TemperatureUnit.FAHRENHEIT -> "°F"
        }

    private fun getCurrentChosenDistanceUnitSymbol(): String = when (preferencesManager.readChosenDistanceUnit()) {
        DistanceUnit.METERS -> "m"
        DistanceUnit.FEET -> "ft"
    }

    private fun getMagneticFieldUnitSymbol(): String = "μT"

    private fun convertMagneticFieldValueToStringWithSymbol(magneticFieldInMicroTesla: Float) =
        magneticFieldInMicroTesla.toString() + " " + getMagneticFieldUnitSymbol()

    private fun convertAngleValueToStringWithSymbol(angleInDegrees: Float): String {
        val convertedAngleValue = convertAngleValueToChosenUnit(angleInDegrees)
        val unitSymbol = getCurrentChosenAngleUnitSymbol()
        return "$convertedAngleValue $unitSymbol"
    }

    private fun convertAngleValueToChosenUnit(angleInRadians: Float): Float =
        when (preferencesManager.readChosenAngleUnit()) {
            AngleUnit.DEGREE -> convertRadiansToDegrees(angleInRadians)
            AngleUnit.RADIAN -> angleInRadians
        }

    private fun convertTemperatureValueToChosenUnit(temperatureInCelsius: Float): Float =
        when (preferencesManager.readChosenTemperatureUnit()) {
            TemperatureUnit.CELSIUS -> temperatureInCelsius
            TemperatureUnit.KELVIN -> convertCelsiusToKelvin(temperatureInCelsius)
            TemperatureUnit.FAHRENHEIT -> convertCelsiusToFahrenheit(temperatureInCelsius)
        }

    private fun convertDistanceValueToStringWithSymbol(distanceInMeters: Float): String {
        val convertedDistanceValue = convertDistanceValueToChosenUnit(distanceInMeters)
        val unitSymbol = getCurrentChosenDistanceUnitSymbol()
        return "$convertedDistanceValue $unitSymbol"
    }

    private fun convertAccelerationValueToStringWithSymbol(accelerationInMeters: Float): String =
        convertDistanceValueToStringWithSymbol(accelerationInMeters) + "/s²"

    private fun convertDistanceValueToChosenUnit(distanceInMeters: Float): Float =
        when (preferencesManager.readChosenDistanceUnit()) {
            DistanceUnit.METERS -> distanceInMeters
            DistanceUnit.FEET -> convertMetersToFeet(distanceInMeters)
        }

    private fun convertRadiansToDegrees(angleInRadians: Float): Float =
        Math.toDegrees(angleInRadians.toDouble()).toFloat()

    private fun convertCelsiusToKelvin(temperatureInCelsius: Float): Float = (temperatureInCelsius + 273.15).toFloat()

    private fun convertCelsiusToFahrenheit(temperatureInCelsius: Float): Float =
        ((9 / 5.0 * temperatureInCelsius) + 32).toFloat()

    private fun convertMetersToFeet(distanceInMeters: Float): Float = (distanceInMeters * 3.28084).toFloat()
}