package com.fluffycat.sensorsmanager.sensors

import android.content.Context
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fluffycat.sensorsmanager.listeners.MagneticFieldListener
import com.fluffycat.sensorsmanager.listeners.MySensorListener
import com.fluffycat.sensorsmanager.utils.tag

class MagneticFieldSensorController(context: Context) : ISensorController {

    override val sensorCurrentData = MutableLiveData<SensorEvent>()

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val magneticFieldListener: MySensorListener = MagneticFieldListener(this)

    override fun startReceivingData() {
        magneticFieldListener.registerListener(sensorManager)
        Log.d(tag, "Started receiving data")
    }

    override fun stopReceivingData() {
        sensorManager.unregisterListener(magneticFieldListener)
        Log.d(tag, "Stopped receiving data")
    }

    override fun onSensorDataReceived(event: SensorEvent) {
        sensorCurrentData.value = event
    }
}