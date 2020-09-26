package com.fluffycat.sensorsmanager.fragments

import android.hardware.SensorEvent
import android.os.Bundle
import android.view.View
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.sensors.ProximitySensorController
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import kotlinx.android.synthetic.main.proximity_fragment.*

class ProximityFragment : BaseChartFragment() {

    override val layoutResource = R.layout.proximity_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        sensorController = ProximitySensorController(sensorManager)
        proximityChart.data = lineData
        proximitySensorInfoLabel.text = sensorController.getSensorInfo()
        fragmentTitle = getString(R.string.proximity)
        chartTitle = getString(R.string.proximity)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDataChanged(event: SensorEvent) {
        val value = event.values[0]
        proximityXValueInfoLabel.text = getString(R.string.distanceChartLabel, value)

        lineData.apply {
            addEntry(Entry(getDataSetByIndex(0).entryCount.toFloat(), value), 0)

            notifyDataChanged()
            proximityChart.notifyDataSetChanged()
            proximityChart.setVisibleXRangeMaximum(100F)
            proximityChart.moveViewTo(entryCount.toFloat(), 0F, YAxis.AxisDependency.RIGHT)
        }
    }
}