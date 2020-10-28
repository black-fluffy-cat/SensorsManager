package com.fluffycat.sensorsmanager.converter

import android.hardware.Sensor
import com.fluffycat.sensorsmanager.preferences.*
import com.fluffycat.sensorsmanager.sensors.*
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mockito.mock
import kotlin.math.round

class ValuesConverterTest {

    @ParameterizedTest
    @MethodSource("numericValuesParams")
    fun `convertValueToChosenUnit should return proper numeric value`(sensorType: Int, distanceUnit: Int,
                                                                      angleUnit: Int,
                                                                      input: Float, expectedOutput: Float) {
        val mockSensor = createMockSensor(sensorType)
        val mockPreferences = createMockPreferencesManager(distanceUnit, angleUnit)
        val valuesConverter = ValuesConverter(mockPreferences)
        assertEquals(expectedOutput, valuesConverter.convertValueToChosenUnit(input, mockSensor))
    }

    @ParameterizedTest
    @MethodSource("stringValuesParams")
    fun `convertValueToChosenUnit should return proper string value`(sensorType: Int, distanceUnit: Int, angleUnit: Int,
                                                                     input: Float, expectedOutput: String) {
        val mockSensor = createMockSensor(sensorType)
        val mockPreferences = createMockPreferencesManager(distanceUnit, angleUnit)
        val valuesConverter = ValuesConverter(mockPreferences)
        assertEquals(expectedOutput, valuesConverter.convertValueToStringWithSymbol(input, mockSensor))
    }

    @ParameterizedTest
    @MethodSource("roundedValuesParams")
    fun `roundValue should return proper rounded values`(input: Float, expectedOutput: Float, decimalPlaces: Int?) {
        val mockPreferences = mock(PreferencesManager::class.java)
        val valuesConverter = ValuesConverter(mockPreferences)
        val roundedValue = if (decimalPlaces == null) {
            valuesConverter.roundValue(input)
        } else {
            valuesConverter.roundValue(input, decimalPlaces)
        }
        assertEquals(expectedOutput, roundedValue)
    }

    private fun createMockPreferencesManager(distanceUnit: Int, angleUnit: Int) =
        mock(PreferencesManager::class.java).apply {
            whenever(readChosenDistanceUnit()).thenReturn(distanceUnit)
            whenever(readChosenAngleUnit()).thenReturn(angleUnit)
        }

    private fun createMockSensor(sensorType: Int) = mock(Sensor::class.java).apply {
        whenever(type).thenReturn(sensorType)
    }

    @Suppress("UNUSED")
    private companion object {

        fun sensorTypes() = SensorValueProvider().getExistingSensors()

        @JvmStatic
        fun numericValuesParams() = listOf(
                Arguments.of(ACCELEROMETER_SENSOR_TYPE, DISTANCE_METERS_VALUE, 0, 1.0f, 1.0f),
                Arguments.of(ACCELEROMETER_SENSOR_TYPE, DISTANCE_FEET_VALUE, 0, 1.0f, 3.28084f),
                Arguments.of(GYROSCOPE_SENSOR_TYPE, 0, ANGLE_DEGREE_VALUE, 1.0f, 57.29578f),
                Arguments.of(GYROSCOPE_SENSOR_TYPE, 0, ANGLE_RAD_VALUE, 1.0f, 1.0f),
                Arguments.of(HEART_RATE_SENSOR_TYPE, 0, 0, 1.0f, 1.0f),
                Arguments.of(LIGHT_SENSOR_TYPE, 0, 0, 1.0f, 1.0f),
                Arguments.of(LINEAR_ACCELERATION_SENSOR_TYPE, DISTANCE_METERS_VALUE, 0, 1.0f, 1.0f),
                Arguments.of(LINEAR_ACCELERATION_SENSOR_TYPE, DISTANCE_FEET_VALUE, 0, 1.0f, 3.28084f),
                Arguments.of(MAGNETIC_FIELD_SENSOR_TYPE, 0, 0, 1.0f, 1.0f),
                Arguments.of(PROXIMITY_SENSOR_TYPE, 0, 0, 1.0f, 1.0f),
                Arguments.of(ROTATION_VECTOR_SENSOR_TYPE, 0, 0, 1.0f, 1.0f))

        @JvmStatic
        fun stringValuesParams() = listOf(
                Arguments.of(ACCELEROMETER_SENSOR_TYPE, DISTANCE_METERS_VALUE, 0, 1.0f, "1.0 m/s²"),
                Arguments.of(ACCELEROMETER_SENSOR_TYPE, DISTANCE_FEET_VALUE, 0, 1.0f, "3.28084 ft/s²"),
                Arguments.of(GYROSCOPE_SENSOR_TYPE, 0, ANGLE_DEGREE_VALUE, 1.0f, "57.29578 °"),
                Arguments.of(GYROSCOPE_SENSOR_TYPE, 0, ANGLE_RAD_VALUE, 1.0f, "1.0 rad"),
                Arguments.of(HEART_RATE_SENSOR_TYPE, 0, 0, 1.0f, "1.0"),
                Arguments.of(LIGHT_SENSOR_TYPE, 0, 0, 1.0f, "Lux: 1.0"),
                Arguments.of(LINEAR_ACCELERATION_SENSOR_TYPE, DISTANCE_METERS_VALUE, 0, 1.0f, "1.0 m"),
                Arguments.of(LINEAR_ACCELERATION_SENSOR_TYPE, DISTANCE_FEET_VALUE, 0, 1.0f, "3.28084 ft"),
                Arguments.of(MAGNETIC_FIELD_SENSOR_TYPE, 0, 0, 1.0f, "1.0 μT"),
                Arguments.of(PROXIMITY_SENSOR_TYPE, 0, 0, 1.0f, "1.0 cm"),
                Arguments.of(ROTATION_VECTOR_SENSOR_TYPE, 0, 0, 1.0f, "1.0"))

        @JvmStatic
        fun roundedValuesParams() = listOf(
                Arguments.of(0.7608197f, 1.0f, 0),
                Arguments.of(0.2576392f, 0.0f, 0),
                Arguments.of(0.7608197f, 0.8f, 1),
                Arguments.of(0.6497015f, 0.6f, 1),
                Arguments.of(1.7758601f, 1.78f, 2),
                Arguments.of(0.3143544f, 0.31f, 2),
                Arguments.of(5.7687119f, 5.769f, 3),
                Arguments.of(3.3791495f, 3.379f, 3),
                Arguments.of(5.5103053f, 5.5103f, 4),
                Arguments.of(7.7060802f, 7.7061f, 4),
                Arguments.of(7.8421046f, 7.84210f, 5),
                Arguments.of(5.3582580f, 5.35826f, 5),
                Arguments.of(0.1980460f, 0.198046f, 6),
                Arguments.of(0.5352279f, 0.535228f, 6),
                Arguments.of(0.2475285f, 0.2475285f, 7),
                Arguments.of(0.7239974f, 0.7239974f, 7),
                Arguments.of(0.0469946f, 0.0469946f, 8),
                Arguments.of(0.4498713f, 0.4498713f, 8),
                Arguments.of(0.8251084f, 0.8251084f, 9),
                Arguments.of(0.0590941f, 0.0590941f, 9),
                Arguments.of(0.4864838f, 0.4864838f, 10),
                Arguments.of(0.0543931f, 0.0543931f, 10),
                Arguments.of(0.8972125f, 0.8972125f, 11),
                Arguments.of(0.1040900f, 0.1040900f, 11),
                Arguments.of(0.9958518f, 0.9958518f, 12),
                Arguments.of(0.9473911f, 0.9473911f, 12),
                Arguments.of(1.7955379f, 1.796f, null),
                Arguments.of(4.8359121f, 4.836f, null)
        )
    }
}