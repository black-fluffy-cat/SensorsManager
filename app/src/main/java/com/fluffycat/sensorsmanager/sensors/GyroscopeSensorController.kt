package com.fluffycat.sensorsmanager.sensors

import android.content.Context
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fluffycat.sensorsmanager.listeners.GyroscopeListener
import com.fluffycat.sensorsmanager.listeners.MySensorListener
import com.fluffycat.sensorsmanager.utils.tag

class GyroscopeSensorController(context: Context) : ISensorController {

    override val sensorCurrentData = MutableLiveData<SensorEvent>()

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val gyroscopeListener: MySensorListener = GyroscopeListener(this)

    override fun startReceivingData() {
        gyroscopeListener.registerListener(sensorManager)
        Log.d(tag, "Started receiving data")
    }

    override fun stopReceivingData() {
        sensorManager.unregisterListener(gyroscopeListener)
        Log.d(tag, "Stopped receiving data")
    }

    override fun onSensorDataReceived(event: SensorEvent) {
        sensorCurrentData.value = event
    }
}