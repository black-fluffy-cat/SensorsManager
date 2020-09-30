package com.fluffycat.sensorsmanager.fragments

import android.os.Bundle
import android.view.View
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.sensors.ACCELEROMETER_SENSOR_TYPE
import com.fluffycat.sensorsmanager.sensors.SensorController

class AccelerometerFragment : BaseChartFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sensorController = SensorController(sensorManager, ACCELEROMETER_SENSOR_TYPE)
        fragmentTitle = getString(R.string.accelerometer)
        chartTitle = getString(R.string.accelerometer)
        super.onViewCreated(view, savedInstanceState)
    }
}