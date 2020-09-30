package com.fluffycat.sensorsmanager.fragments

import android.os.Bundle
import android.view.View
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.sensors.MagneticFieldSensorController

class MagneticFieldFragment : BaseChartFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sensorController = MagneticFieldSensorController(sensorManager)
        fragmentTitle = getString(R.string.magnetic_field)
        chartTitle = getString(R.string.magnetic_field)
        super.onViewCreated(view, savedInstanceState)
    }
}