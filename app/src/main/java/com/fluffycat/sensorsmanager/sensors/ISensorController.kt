package com.fluffycat.sensorsmanager.sensors

import android.hardware.SensorEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

interface ISensorController {

    val sensorCurrentData: MutableSharedFlow<SensorEvent?>
    val additionalData: MutableSharedFlow<Int?>

    fun observeSensorCurrentData(): SharedFlow<SensorEvent?> = sensorCurrentData
    fun observeAdditionalData(): SharedFlow<Int?> = additionalData

    fun startReceivingData(): Boolean
    fun stopReceivingData()
    fun onSensorDataReceived(event: SensorEvent)
    fun getSensorInfo(): String
    fun onAdditionalDataChanged(accuracy: Int)
}