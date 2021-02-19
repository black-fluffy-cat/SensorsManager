package com.fluffycat.sensorsmanager.sensors

import com.fluffycat.sensorsmanager.rest.DefaultCallback
import com.fluffycat.sensorsmanager.rest.SensorValuesCall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SensorValueCollector {

    companion object {
        private const val MAX_VALUES_BUFFER_SIZE = 200
    }

    private val sensorValuesCall = SensorValuesCall()
    private val sendValuesCallback = DefaultCallback()

    private val valuesBuffer: MutableList<Triple<Float, Float, Float>> = mutableListOf()

    @Synchronized
    fun addValues(values: Triple<Float, Float, Float>) {
        valuesBuffer.add(values)
        if (valuesBuffer.size >= MAX_VALUES_BUFFER_SIZE) {
            val listToSend = valuesBuffer.toList()
            valuesBuffer.clear()
            sendListToServer(listToSend)
        }
    }

    private fun sendListToServer(listToSend: List<Triple<Float, Float, Float>>) {
        CoroutineScope(Dispatchers.IO).launch {
            val sensorValues = SensorValues(listToSend)
            sensorValuesCall.postSensorValues(sensorValues, sendValuesCallback)
        }
    }
}