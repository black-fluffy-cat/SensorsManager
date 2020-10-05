package com.fluffycat.sensorsmanager.sensors

import android.hardware.SensorEvent
import androidx.lifecycle.MutableLiveData

interface ISensorController {

    val sensorCurrentData: MutableLiveData<SensorEvent>
    val additionalData: MutableLiveData<Int>

    fun startReceivingData(): Boolean
    fun stopReceivingData()
    fun onSensorDataReceived(event: SensorEvent)
    fun getSensorInfo(): String
    fun onAdditionalDataChanged(accuracy: Int)
}