package com.fluffycat.sensorsmanager.listeners

import android.hardware.Sensor
import android.hardware.SensorEvent
import com.fluffycat.sensorsmanager.sensors.ISensorController
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

class UniversalSensorListenerTest {

    @Mock
    private lateinit var dataCollector: ISensorController

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `onSensorChanged should call onSensorDataReceived with propagated sensorEvent`() {
        val universalSensorListener = UniversalSensorListener(dataCollector)
        val sensorEvent = mock(SensorEvent::class.java)

        universalSensorListener.onSensorChanged(sensorEvent)
        verify(dataCollector).onSensorDataReceived(sensorEvent)
    }

    @Test
    fun `onAccuracyChanged should call onAdditionalDataChanged with propagated accuracy`() {
        val universalSensorListener = UniversalSensorListener(dataCollector)
        val sensorMock = mock(Sensor::class.java)
        val accuracy = 10

        universalSensorListener.onAccuracyChanged(sensorMock, 10)
        verify(dataCollector).onAdditionalDataChanged(accuracy)
    }
}