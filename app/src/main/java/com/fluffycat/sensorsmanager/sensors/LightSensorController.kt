package com.fluffycat.sensorsmanager.sensors

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.util.Log
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.listeners.LightSensorListener
import com.fluffycat.sensorsmanager.listeners.OneAxisSensorListener
import com.fluffycat.sensorsmanager.utils.tag
import com.github.mikephil.charting.charts.LineChart

class LightSensorController(activity: Activity) : ISensorController {

    private val lightSensorChart: LineChart? = activity.findViewById(R.id.lightChart)
    private var lightSensorListener: OneAxisSensorListener? = null

    private var sensorManager = activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    override fun startReceivingData() {
        lightSensorChart?.let {
            lightSensorListener = LightSensorListener(it)
            sensorManager.registerListener(lightSensorListener,
                    sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_GAME)
            Log.d(tag, "Started receiving data")
        }
    }

    override fun stopReceivingData() {
        sensorManager.unregisterListener(lightSensorListener)
        Log.d(tag, "Stopped receiving data")
    }
}