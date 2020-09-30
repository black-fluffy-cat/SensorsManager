package com.fluffycat.sensorsmanager.fragments

import android.os.Bundle
import android.view.View
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.sensors.AccelerometerSensorController

class AccelerometerFragment : BaseChartFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sensorController = AccelerometerSensorController(sensorManager)
        fragmentTitle = getString(R.string.accelerometer)
        chartTitle = getString(R.string.accelerometer)
        super.onViewCreated(view, savedInstanceState)
    }
}