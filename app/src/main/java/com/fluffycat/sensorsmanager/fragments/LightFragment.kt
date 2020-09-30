package com.fluffycat.sensorsmanager.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.sensors.LIGHT_SENSOR_TYPE
import com.fluffycat.sensorsmanager.sensors.SensorController
import com.github.mikephil.charting.data.LineData

class LightFragment : BaseChartFragment() {

    override fun createLineData() = LineData(createDataSet(Color.YELLOW, chartTitle))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sensorController = SensorController(sensorManager, LIGHT_SENSOR_TYPE)
        fragmentTitle = getString(R.string.light)
        chartTitle = "Light power"
        super.onViewCreated(view, savedInstanceState)
    }
}