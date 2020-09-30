package com.fluffycat.sensorsmanager.fragments

import android.os.Bundle
import android.view.View
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.sensors.ProximitySensorController

class ProximityFragment : BaseChartFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sensorController = ProximitySensorController(sensorManager)
        fragmentTitle = getString(R.string.proximity)
        chartTitle = getString(R.string.proximity)
        super.onViewCreated(view, savedInstanceState)
    }
}