package com.fluffycat.sensorsmanager.converter

import android.hardware.Sensor
import com.fluffycat.sensorsmanager.SensorsManagerApplication
import com.fluffycat.sensorsmanager.preferences.*
import kotlin.math.pow
import kotlin.math.roundToInt

class ValuesConverter(private val preferencesManager: PreferencesManager =
                          SensorsManagerApplication.instance.preferencesManager) {

    private fun getCurrentChosenAngleUnitSymbol(): String = when (preferencesManager.readChosenAngleUnit()) {
        ANGLE_DEGREE_VALUE -> "°"
        ANGLE_RAD_VALUE -> "rad"
        else -> "rad"
    }

    private fun getCurrentChosenTemperatureUnitSymbol(): String = when (preferencesManager.readChosenTemperatureUnit()) {
        TEMPERATURE_CELSIUS_VALUE -> "°C"
        TEMPERATURE_KELVIN_VALUE -> "K"
        TEMPERATURE_FAHRENHEIT_VALUE -> "°F"
        else -> "°C"
    }

    private fun getCurrentChosenDistanceUnitSymbol(): String = when (preferencesManager.readChosenDistanceUnit()) {
        DISTANCE_METERS_VALUE -> "m"
        DISTANCE_FEET_VALUE -> "ft"
        else -> "m"
    }

    private fun getMagneticFieldUnitSymbol(): String = "μT"

    private fun convertMagneticFieldValueToStringWithSymbol(magneticFieldInMicroTesla: Float) =
        magneticFieldInMicroTesla.toString() + " " + getMagneticFieldUnitSymbol()

    private fun convertAngleValueToStringWithSymbol(angleInDegrees: Float): String {
        val convertedAngleValue = convertAngleValueToChosenUnit(angleInDegrees)
        val unitSymbol = getCurrentChosenAngleUnitSymbol()
        return "$convertedAngleValue $unitSymbol"
    }

    //  TODO where it have been and why it is gone
    fun convertAngularVelocityValueToStringWithSymbol(angularVelocityInDegrees: Float): String =
        convertAngleValueToStringWithSymbol(angularVelocityInDegrees) + "/s"

    private fun convertAngleValueToChosenUnit(angleInRadians: Float): Float = when (preferencesManager.readChosenAngleUnit()) {
        ANGLE_DEGREE_VALUE -> convertRadiansToDegrees(angleInRadians)
        ANGLE_RAD_VALUE -> angleInRadians
        else -> angleInRadians
    }

    fun convertTemperatureValueToStringWithSymbol(temperatureInCelsius: Float): String {
        val convertedTemperatureValue = convertTemperatureValueToChosenUnit(temperatureInCelsius)
        val unitSymbol = getCurrentChosenTemperatureUnitSymbol()
        return "$convertedTemperatureValue $unitSymbol"
    }

    private fun convertTemperatureValueToChosenUnit(temperatureInCelsius: Float): Float =
        when (preferencesManager.readChosenTemperatureUnit()) {
            TEMPERATURE_CELSIUS_VALUE -> temperatureInCelsius
            TEMPERATURE_KELVIN_VALUE -> convertCelsiusToKelvin(temperatureInCelsius)
            TEMPERATURE_FAHRENHEIT_VALUE -> convertCelsiusToFahrenheit(temperatureInCelsius)
            else -> temperatureInCelsius
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
            DISTANCE_METERS_VALUE -> distanceInMeters
            DISTANCE_FEET_VALUE -> convertMetersToFeet(distanceInMeters)
            else -> distanceInMeters
        }

    fun roundValue(value: Float, decimalPlaces: Int = 3): Float {
        val decimalHelperValue = 10.0.pow(decimalPlaces)
        return ((value * decimalHelperValue).roundToInt()) / decimalHelperValue.toFloat()
    }

    // TODO Check what types sensors does return (float -> double)
    private fun convertRadiansToDegrees(angleInDegrees: Float): Float =
        Math.toDegrees(angleInDegrees.toDouble()).toFloat()

    // TODO Check what types sensors does return (float -> double)
    private fun convertCelsiusToKelvin(temperatureInCelsius: Float): Float = (temperatureInCelsius + 273.15).toFloat()

    // TODO Check what types sensors does return (float -> double)
    private fun convertCelsiusToFahrenheit(temperatureInCelsius: Float): Float =
        ((9 / 5.0 * temperatureInCelsius) + 32).toFloat()

    // TODO Check what types sensors does return (float -> double)
    private fun convertMetersToFeet(distanceInMeters: Float): Float = (distanceInMeters * 3.2808399).toFloat()

    fun convertValueToChosenUnit(value: Float, sensor: Sensor?): Float {
        return when(sensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> convertDistanceValueToChosenUnit(value)
            Sensor.TYPE_GYROSCOPE -> convertAngleValueToChosenUnit(value)
            Sensor.TYPE_HEART_RATE -> value
            Sensor.TYPE_LIGHT -> value
            Sensor.TYPE_LINEAR_ACCELERATION -> convertDistanceValueToChosenUnit(value)
            Sensor.TYPE_MAGNETIC_FIELD -> value
            Sensor.TYPE_PROXIMITY -> value
            Sensor.TYPE_ROTATION_VECTOR -> value
            else -> value
        }
    }

    fun convertValueToStringWithSymbol(value: Float, sensor: Sensor?): String {
        return when(sensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> convertAccelerationValueToStringWithSymbol(value)
            Sensor.TYPE_GYROSCOPE -> convertAngleValueToStringWithSymbol(value)
            Sensor.TYPE_HEART_RATE -> value.toString()
            Sensor.TYPE_LIGHT -> value.toString()
            Sensor.TYPE_LINEAR_ACCELERATION -> convertDistanceValueToStringWithSymbol(value)
            Sensor.TYPE_MAGNETIC_FIELD -> convertMagneticFieldValueToStringWithSymbol(value)
            Sensor.TYPE_PROXIMITY -> value.toString()
            Sensor.TYPE_ROTATION_VECTOR -> value.toString()
            else -> value.toString()
        }
    }
}