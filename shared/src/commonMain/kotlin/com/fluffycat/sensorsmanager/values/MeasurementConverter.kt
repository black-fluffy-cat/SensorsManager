package com.fluffycat.sensorsmanager.values

import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * Platform-independent unit conversions and value rounding shared between the
 * Android and iOS applications. All sensors report [Float] values.
 */
class MeasurementConverter {

    fun roundValue(value: Float, decimalPlaces: Int = 3): Float {
        if (decimalPlaces < 0 || decimalPlaces > 7) return value
        val decimalHelperValue = 10.0.pow(decimalPlaces)
        return ((value * decimalHelperValue).roundToInt()) / decimalHelperValue.toFloat()
    }

    fun convertDistance(distanceInMeters: Float, unit: DistanceUnit): Float = when (unit) {
        DistanceUnit.METERS -> distanceInMeters
        DistanceUnit.FEET -> metersToFeet(distanceInMeters)
    }

    fun convertAngle(angleInRadians: Float, unit: AngleUnit): Float = when (unit) {
        AngleUnit.DEGREE -> radiansToDegrees(angleInRadians)
        AngleUnit.RADIAN -> angleInRadians
    }

    fun convertTemperature(temperatureInCelsius: Float, unit: TemperatureUnit): Float = when (unit) {
        TemperatureUnit.CELSIUS -> temperatureInCelsius
        TemperatureUnit.KELVIN -> celsiusToKelvin(temperatureInCelsius)
        TemperatureUnit.FAHRENHEIT -> celsiusToFahrenheit(temperatureInCelsius)
    }

    fun distanceSymbol(unit: DistanceUnit): String = when (unit) {
        DistanceUnit.METERS -> "m"
        DistanceUnit.FEET -> "ft"
    }

    fun angleSymbol(unit: AngleUnit): String = when (unit) {
        AngleUnit.DEGREE -> "°"
        AngleUnit.RADIAN -> "rad"
    }

    fun temperatureSymbol(unit: TemperatureUnit): String = when (unit) {
        TemperatureUnit.CELSIUS -> "°C"
        TemperatureUnit.KELVIN -> "K"
        TemperatureUnit.FAHRENHEIT -> "°F"
    }

    fun magneticFieldSymbol(): String = "μT"

    private fun metersToFeet(distanceInMeters: Float): Float = (distanceInMeters * 3.28084).toFloat()

    private fun radiansToDegrees(angleInRadians: Float): Float =
        (angleInRadians.toDouble() * 180.0 / PI).toFloat()

    private fun celsiusToKelvin(temperatureInCelsius: Float): Float = (temperatureInCelsius + 273.15).toFloat()

    private fun celsiusToFahrenheit(temperatureInCelsius: Float): Float =
        ((9 / 5.0 * temperatureInCelsius) + 32).toFloat()
}
