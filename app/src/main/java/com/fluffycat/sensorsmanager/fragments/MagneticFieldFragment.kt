package com.fluffycat.sensorsmanager.fragments

import android.os.Bundle
import android.view.View
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.sensors.MAGNETIC_FIELD_SENSOR_TYPE
import com.fluffycat.sensorsmanager.sensors.SensorController

class MagneticFieldFragment : BaseChartFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sensorController = SensorController(sensorManager, MAGNETIC_FIELD_SENSOR_TYPE)
        fragmentTitle = getString(R.string.magnetic_field)
        chartTitle = getString(R.string.magnetic_field)
        super.onViewCreated(view, savedInstanceState)
    }
}