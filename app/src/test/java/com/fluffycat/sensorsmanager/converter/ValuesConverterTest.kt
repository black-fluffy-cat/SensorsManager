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

class ValuesConverterTest {

    @Suppress("UNUSED")
    private companion object {
        fun sensorTypes() = SensorValueProvider().getExistingSensors()

        @JvmStatic
        fun expectedValues() = listOf<Arguments>(
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
    }

    @BeforeEach
    fun setup() {
    }

    @ParameterizedTest
    @MethodSource("expectedValues")
    fun `convertValueToChosenUnit should return proper value`(sensorType: Int, distanceUnit: Int, angleUnit: Int,
                                                              inputValue: Float, expectedOutput: Float) {
        val mockSensor = createMockSensor(sensorType)
        val mockPreferences = createMockPreferencesManager(distanceUnit, angleUnit)
        val valuesConverter = ValuesConverter(mockPreferences)
        assertEquals(expectedOutput, valuesConverter.convertValueToChosenUnit(inputValue, mockSensor))
    }

    private fun createMockPreferencesManager(distanceUnit: Int, angleUnit: Int) =
        mock(PreferencesManager::class.java).apply {
            whenever(readChosenDistanceUnit()).thenReturn(distanceUnit)
            whenever(readChosenAngleUnit()).thenReturn(angleUnit)
        }

    private fun createMockSensor(sensorType: Int) = mock(Sensor::class.java).apply {
        whenever(type).thenReturn(sensorType)
    }
}