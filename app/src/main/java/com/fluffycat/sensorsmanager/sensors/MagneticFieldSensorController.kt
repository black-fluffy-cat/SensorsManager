package com.fluffycat.sensorsmanager.sensors

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.listeners.MagneticFieldListener
import com.fluffycat.sensorsmanager.utils.tag
import com.github.mikephil.charting.charts.LineChart

class MagneticFieldSensorController(activity: Activity) : ISensorController {

    private val magneticFieldChart: LineChart? = activity.findViewById(R.id.magneticFieldChart)
    private var magneticFieldListener: SensorEventListener? = null

    private var sensorManager = activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    override fun startReceivingData() {
        magneticFieldChart?.let {
            magneticFieldListener = MagneticFieldListener(it)
            sensorManager.registerListener(magneticFieldListener,
                    sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME)
            Log.d(tag, "Started receiving data")
        }
    }

    override fun stopReceivingData() {
        sensorManager.unregisterListener(magneticFieldListener)
        Log.d(tag, "Stopped receiving data")
    }
}