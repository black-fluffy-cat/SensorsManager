package com.fluffycat.sensorsmanager.values

interface MeasurementUnit

enum class DistanceUnit : MeasurementUnit {
    METERS, FEET
}

enum class AngleUnit : MeasurementUnit {
    DEGREE, RADIAN
}

enum class TemperatureUnit : MeasurementUnit {
    CELSIUS, KELVIN, FAHRENHEIT
}

class UnitsProvider {

    companion object {
        private val supportedDistanceUnits = arrayOf(DistanceUnit.METERS, DistanceUnit.FEET)
    }

    fun getAvailableDistanceUnits() = supportedDistanceUnits
}