package com.fluffycat.sensorsmanager.fragments

import android.os.Bundle
import android.view.View
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.sensors.LINEAR_ACCELERATION_SENSOR_TYPE
import com.fluffycat.sensorsmanager.sensors.SensorController

class LinearAccelerationFragment : BaseChartFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sensorController = SensorController(sensorManager, LINEAR_ACCELERATION_SENSOR_TYPE)
        fragmentTitle = getString(R.string.linearAcceleration)
        chartTitle = getString(R.string.linearAcceleration)
        super.onViewCreated(view, savedInstanceState)
    }
}