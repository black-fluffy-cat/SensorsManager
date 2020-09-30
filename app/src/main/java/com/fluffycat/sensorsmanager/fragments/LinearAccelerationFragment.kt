package com.fluffycat.sensorsmanager.fragments

import android.os.Bundle
import android.view.View
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.sensors.LinearAccelerationSensorController

class LinearAccelerationFragment : BaseChartFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sensorController = LinearAccelerationSensorController(sensorManager)
        fragmentTitle = getString(R.string.linearAcceleration)
        chartTitle = getString(R.string.linearAcceleration)
        super.onViewCreated(view, savedInstanceState)
    }
}