package com.fluffycat.sensorsmanager.sensors

import android.content.Context
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fluffycat.sensorsmanager.listeners.LightSensorListener
import com.fluffycat.sensorsmanager.listeners.MySensorListener
import com.fluffycat.sensorsmanager.utils.tag

class LightSensorController(context: Context) : ISensorController {

    override val sensorCurrentData = MutableLiveData<SensorEvent>()

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val lightSensorListener: MySensorListener = LightSensorListener(this)

    override fun startReceivingData() {
        lightSensorListener.registerListener(sensorManager)
        Log.d(tag, "Started receiving data")
    }

    override fun stopReceivingData() {
        sensorManager.unregisterListener(lightSensorListener)
        Log.d(tag, "Stopped receiving data")
    }

    override fun onSensorDataReceived(event: SensorEvent) {
        sensorCurrentData.value = event
    }
}