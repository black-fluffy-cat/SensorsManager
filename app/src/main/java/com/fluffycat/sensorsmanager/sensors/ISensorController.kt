package com.fluffycat.sensorsmanager.sensors

import android.hardware.SensorEvent
import androidx.lifecycle.MutableLiveData

interface ISensorController {

    val sensorCurrentData: MutableLiveData<SensorEvent>

    fun startReceivingData()
    fun stopReceivingData()
    fun onSensorDataReceived(event: SensorEvent)
    fun getSensorInfo(): String
}