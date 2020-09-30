package com.fluffycat.sensorsmanager.fragments

import android.os.Bundle
import android.view.View
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.sensors.GYROSCOPE_SENSOR_TYPE
import com.fluffycat.sensorsmanager.sensors.SensorController

class GyroscopeFragment : BaseChartFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sensorController = SensorController(sensorManager, GYROSCOPE_SENSOR_TYPE)
        fragmentTitle = getString(R.string.gyroscope)
        chartTitle = getString(R.string.gyroscope)
        super.onViewCreated(view, savedInstanceState)
    }
}