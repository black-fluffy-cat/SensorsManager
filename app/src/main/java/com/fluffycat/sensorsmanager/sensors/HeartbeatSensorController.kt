package com.fluffycat.sensorsmanager.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fluffycat.sensorsmanager.listeners.HeartbeatListener
import com.fluffycat.sensorsmanager.listeners.UniversalSensorListener
import com.fluffycat.sensorsmanager.utils.tag

private const val HEART_RATE_SENSOR_TYPE = Sensor.TYPE_HEART_RATE

class HeartbeatSensorController(context: Context) : BaseSensorController(context, HEART_RATE_SENSOR_TYPE) {

    val additionalData = MutableLiveData<Int>()

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val heartbeatListener: UniversalSensorListener = HeartbeatListener(this)

    override fun startReceivingData() {
        sensorManager.registerListener(heartbeatListener, sensorManager.getDefaultSensor(HEART_RATE_SENSOR_TYPE),
                SensorManager.SENSOR_DELAY_GAME)
        Log.d(tag, "Started receiving data")
    }

    override fun stopReceivingData() {
        sensorManager.unregisterListener(heartbeatListener)
        Log.d(tag, "Stopped receiving data")
    }

    fun onAdditionalDataChanged(code: Int) {
        additionalData.value = code
    }
}