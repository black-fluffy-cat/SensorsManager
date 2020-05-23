package com.fluffycat.sensorsmanager.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fluffycat.sensorsmanager.SensorsManagerApplication
import com.fluffycat.sensorsmanager.listeners.HeartbeatListener
import com.fluffycat.sensorsmanager.listeners.UniversalSensorListener
import com.fluffycat.sensorsmanager.utils.tag

private val HEART_RATE_SENSOR_TYPE = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
    Sensor.TYPE_HEART_RATE
} else {
    -1
}

class HeartbeatSensorController(context: Context) : BaseSensorController(context, HEART_RATE_SENSOR_TYPE) {

    companion object {
        fun doesSensorExist(): Boolean = (SensorsManagerApplication.getContext()
            .getSystemService(Context.SENSOR_SERVICE) as SensorManager).getDefaultSensor(
                HEART_RATE_SENSOR_TYPE) != null
    }

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