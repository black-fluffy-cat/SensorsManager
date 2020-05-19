package com.fluffycat.sensorsmanager.converter

import com.fluffycat.sensorsmanager.SensorsManagerApplication
import com.fluffycat.sensorsmanager.preferences.*
import kotlin.math.pow
import kotlin.math.roundToInt

class ValuesConverter(private val preferencesManager: PreferencesManager =
                          SensorsManagerApplication.instance.preferencesManager) {

    fun getCurrentChosenAngleUnitString(): String = when (preferencesManager.readChosenAngleUnit()) {
        ANGLE_DEGREE_VALUE -> "°"
        ANGLE_RAD_VALUE -> "rad"
        else -> "°"
    }

    fun getCurrentChosenTemperatureUnitString(): String = when (preferencesManager.readChosenTemperatureUnit()) {
        TEMPERATURE_CELSIUS_VALUE -> "°C"
        TEMPERATURE_KELVIN_VALUE -> "K"
        TEMPERATURE_FAHRENHEIT_VALUE -> "°F"
        else -> "°C"
    }

    fun getCurrentChosenDistanceUnitString(): String = when (preferencesManager.readChosenDistanceUnit()) {
        DISTANCE_METERS_VALUE -> "m"
        DISTANCE_FEET_VALUE -> "ft"
        else -> "m"
    }

    fun convertAngleValueToString(angleInDegrees: Float): String {
        return when {
            preferencesManager.readChosenAngleUnit() == ANGLE_DEGREE_VALUE -> {
                "$angleInDegrees°"
            }
            preferencesManager.readChosenAngleUnit() == ANGLE_RAD_VALUE -> {
                "${convertDegreesToRadians(angleInDegrees)} rad"
            }
            else -> {
                "$angleInDegrees°"
            }
        }
    }

    fun convertAngleValueToChosenUnit(angleInDegrees: Float): Float {
        return when {
            preferencesManager.readChosenAngleUnit() == ANGLE_DEGREE_VALUE -> {
                angleInDegrees
            }
            preferencesManager.readChosenAngleUnit() == ANGLE_RAD_VALUE -> {
                convertDegreesToRadians(angleInDegrees)
            }
            else -> {
                angleInDegrees
            }
        }
    }

    fun convertTemperatureValueToString(temperatureInCelsius: Float): String {
        return when {
            preferencesManager.readChosenTemperatureUnit() == TEMPERATURE_CELSIUS_VALUE -> {
                "$temperatureInCelsius °C"
            }
            preferencesManager.readChosenTemperatureUnit() == TEMPERATURE_KELVIN_VALUE -> {
                "${convertCelsiusToKelvin(temperatureInCelsius)} K"
            }
            preferencesManager.readChosenTemperatureUnit() == TEMPERATURE_FAHRENHEIT_VALUE -> {
                "${convertCelsiusToFahrenheit(temperatureInCelsius)} °F"
            }
            else -> {
                "$temperatureInCelsius °C"
            }
        }
    }

    fun convertTemperatureValueToChosenUnit(temperatureInCelsius: Float): Float {
        return when {
            preferencesManager.readChosenTemperatureUnit() == TEMPERATURE_CELSIUS_VALUE -> {
                temperatureInCelsius
            }
            preferencesManager.readChosenTemperatureUnit() == TEMPERATURE_KELVIN_VALUE -> {
                convertCelsiusToKelvin(temperatureInCelsius)
            }
            preferencesManager.readChosenTemperatureUnit() == TEMPERATURE_FAHRENHEIT_VALUE -> {
                convertCelsiusToFahrenheit(temperatureInCelsius)
            }
            else -> {
                temperatureInCelsius
            }
        }
    }

    fun convertDistanceValueToString(distanceInMeters: Float): String {
        return when {
            preferencesManager.readChosenDistanceUnit() == DISTANCE_METERS_VALUE -> {
                "$distanceInMeters m"
            }
            preferencesManager.readChosenDistanceUnit() == DISTANCE_FEET_VALUE -> {
                convertMetersToFeet(distanceInMeters).toString() + " ft"
            }
            else -> {
                "$distanceInMeters m"
            }
        }
    }

    fun convertDistanceValueToChosenUnit(distanceInMeters: Float): Float {
        return when {
            preferencesManager.readChosenDistanceUnit() == DISTANCE_METERS_VALUE -> {
                distanceInMeters
            }
            preferencesManager.readChosenDistanceUnit() == DISTANCE_FEET_VALUE -> {
                convertMetersToFeet(distanceInMeters)
            }
            else -> {
                distanceInMeters
            }
        }
    }

    fun roundValue(value: Float, decimalPlaces: Int = 3): Float {
        val decimalHelperValue = 10.0.pow(decimalPlaces)
        return ((value * decimalHelperValue).roundToInt()) / decimalHelperValue.toFloat()
    }

    // TODO Check what types sensors does return (float -> double)
    private fun convertDegreesToRadians(angleInDegrees: Float): Float =
        Math.toRadians(angleInDegrees.toDouble()).toFloat()

    // TODO Check what types sensors does return (float -> double)
    private fun convertCelsiusToKelvin(temperatureInCelsius: Float): Float = (temperatureInCelsius + 273.15).toFloat()

    // TODO Check what types sensors does return (float -> double)
    private fun convertCelsiusToFahrenheit(temperatureInCelsius: Float): Float =
        ((9 / 5.0 * temperatureInCelsius) + 32).toFloat()

    // TODO Check what types sensors does return (float -> double)
    private fun convertMetersToFeet(distanceInMeters: Float): Float = (distanceInMeters * 3.2808399).toFloat()
}