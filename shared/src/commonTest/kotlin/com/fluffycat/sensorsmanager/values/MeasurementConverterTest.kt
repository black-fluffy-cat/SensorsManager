package com.fluffycat.sensorsmanager.values

import kotlin.test.Test
import kotlin.test.assertEquals

class MeasurementConverterTest {

    private val converter = MeasurementConverter()

    @Test
    fun metersAreConvertedToFeet() {
        assertEquals(1.0f, converter.convertDistance(1.0f, DistanceUnit.METERS))
        assertEquals(3.28084f, converter.convertDistance(1.0f, DistanceUnit.FEET))
    }

    @Test
    fun radiansAreConvertedToDegrees() {
        assertEquals(1.0f, converter.convertAngle(1.0f, AngleUnit.RADIAN))
        assertEquals(57.29578f, converter.convertAngle(1.0f, AngleUnit.DEGREE))
    }

    @Test
    fun celsiusIsConvertedToOtherTemperatureScales() {
        assertEquals(0.0f, converter.convertTemperature(0.0f, TemperatureUnit.CELSIUS))
        assertEquals(273.15f, converter.convertTemperature(0.0f, TemperatureUnit.KELVIN))
        assertEquals(32.0f, converter.convertTemperature(0.0f, TemperatureUnit.FAHRENHEIT))
    }

    @Test
    fun valuesAreRoundedToTheRequestedNumberOfDecimalPlaces() {
        assertEquals(5.769f, converter.roundValue(5.7687119f, 3))
        assertEquals(1.0f, converter.roundValue(0.7608197f, 0))
        assertEquals(1.796f, converter.roundValue(1.7955379f))
        assertEquals(0.2475285f, converter.roundValue(0.2475285f, 7))
    }

    @Test
    fun roundingIsSkippedForOutOfRangePrecision() {
        assertEquals(0.0469946f, converter.roundValue(0.0469946f, 8))
    }
}
