package com.fluffycat.sensorsmanager.sensors

import android.app.Activity
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.listeners.AccelerometerListener
import com.fluffycat.sensorsmanager.utils.tag
import com.github.mikephil.charting.charts.LineChart

class AccelerometerSensorController(activity: Activity) : ISensorController {

    private val accelerometerChart: LineChart? = activity.findViewById(R.id.accelerometerChart)
    private var accelerometerListener: SensorEventListener? = null

    //TODO activity or applicationContext ??
    var sensorManager = activity.getSystemService(SENSOR_SERVICE) as SensorManager

    override fun startReceivingData() {
        Log.d(tag, "startReceivingData")
        accelerometerChart?.let {
            accelerometerListener = AccelerometerListener(it)
            sensorManager.registerListener(accelerometerListener,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun stopReceivingData() {
        Log.d(tag, "stopReceivingData")
        sensorManager.unregisterListener(accelerometerListener)
    }
}