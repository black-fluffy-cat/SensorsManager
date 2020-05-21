package com.fluffycat.sensorsmanager.converter

import com.fluffycat.sensorsmanager.SensorsManagerApplication
import com.fluffycat.sensorsmanager.preferences.*
import kotlin.math.pow
import kotlin.math.roundToInt

class ValuesConverter(private val preferencesManager: PreferencesManager =
                          SensorsManagerApplication.instance.preferencesManager) {

    fun getCurrentChosenAngleUnitSymbol(): String = when (preferencesManager.readChosenAngleUnit()) {
        ANGLE_DEGREE_VALUE -> "°"
        ANGLE_RAD_VALUE -> "rad"
        else -> "rad"
    }

    fun getCurrentChosenTemperatureUnitSymbol(): String = when (preferencesManager.readChosenTemperatureUnit()) {
        TEMPERATURE_CELSIUS_VALUE -> "°C"
        TEMPERATURE_KELVIN_VALUE -> "K"
        TEMPERATURE_FAHRENHEIT_VALUE -> "°F"
        else -> "°C"
    }

    fun getCurrentChosenDistanceUnitSymbol(): String = when (preferencesManager.readChosenDistanceUnit()) {
        DISTANCE_METERS_VALUE -> "m"
        DISTANCE_FEET_VALUE -> "ft"
        else -> "m"
    }

    fun getMagneticFieldUnitSymbol(): String = "μT"

    fun convertMagneticFieldValueToStringWithSymbol(magneticFieldInMicroTesla: Float) =
        magneticFieldInMicroTesla.toString() + " " + getMagneticFieldUnitSymbol()

    fun convertAngleValueToStringWithSymbol(angleInDegrees: Float): String {
        val convertedAngleValue = convertAngleValueToChosenUnit(angleInDegrees)
        val unitSymbol = getCurrentChosenAngleUnitSymbol()
        return "$convertedAngleValue $unitSymbol"
    }

    fun convertAngularVelocityValueToStringWithSymbol(angularVelocityInDegrees: Float): String =
        convertAngleValueToStringWithSymbol(angularVelocityInDegrees) + "/s"

    fun convertAngleValueToChosenUnit(angleInRadians: Float): Float = when (preferencesManager.readChosenAngleUnit()) {
        ANGLE_DEGREE_VALUE -> convertRadiansToDegrees(angleInRadians)
        ANGLE_RAD_VALUE -> angleInRadians
        else -> angleInRadians
    }

    fun convertTemperatureValueToStringWithSymbol(temperatureInCelsius: Float): String {
        val convertedTemperatureValue = convertTemperatureValueToChosenUnit(temperatureInCelsius)
        val unitSymbol = getCurrentChosenTemperatureUnitSymbol()
        return "$convertedTemperatureValue $unitSymbol"
    }

    fun convertTemperatureValueToChosenUnit(temperatureInCelsius: Float): Float =
        when (preferencesManager.readChosenTemperatureUnit()) {
            TEMPERATURE_CELSIUS_VALUE -> temperatureInCelsius
            TEMPERATURE_KELVIN_VALUE -> convertCelsiusToKelvin(temperatureInCelsius)
            TEMPERATURE_FAHRENHEIT_VALUE -> convertCelsiusToFahrenheit(temperatureInCelsius)
            else -> temperatureInCelsius
        }

    fun convertDistanceValueToStringWithSymbol(distanceInMeters: Float): String {
        val convertedDistanceValue = convertDistanceValueToChosenUnit(distanceInMeters)
        val unitSymbol = getCurrentChosenDistanceUnitSymbol()
        return "$convertedDistanceValue $unitSymbol"
    }

    fun convertAccelerationValueToStringWithSymbol(accelerationInMeters: Float): String =
        convertDistanceValueToStringWithSymbol(accelerationInMeters) + "/s²"

    fun convertDistanceValueToChosenUnit(distanceInMeters: Float): Float =
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
}