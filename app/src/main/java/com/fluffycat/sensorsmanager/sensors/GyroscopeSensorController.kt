package com.fluffycat.sensorsmanager.sensors

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.util.Log
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.listeners.GyroscopeListener
import com.fluffycat.sensorsmanager.listeners.ThreeAxisSensorListener
import com.fluffycat.sensorsmanager.utils.tag
import com.github.mikephil.charting.charts.LineChart

class GyroscopeSensorController(activity: Activity) : ISensorController {

    private val gyroscopeChart: LineChart? = activity.findViewById(R.id.gyroscopeChart)
    private var gyroscopeListener: ThreeAxisSensorListener? = null

    private var sensorManager = activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    override fun startReceivingData() {
        gyroscopeChart?.let {
            gyroscopeListener = GyroscopeListener(it)
            sensorManager.registerListener(gyroscopeListener,
                    sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_GAME)
            Log.d(tag, "Started receiving data")
        }
    }

    override fun stopReceivingData() {
        sensorManager.unregisterListener(gyroscopeListener)
        Log.d(tag, "Stopped receiving data")
    }
}