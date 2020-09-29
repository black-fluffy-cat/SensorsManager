package com.fluffycat.sensorsmanager.fragments

import android.hardware.SensorEvent
import android.os.Bundle
import android.view.View
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.sensors.ProximitySensorController
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import kotlinx.android.synthetic.main.chart_fragment.*

class ProximityFragment : BaseChartFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sensorController = ProximitySensorController(sensorManager)
        fragmentTitle = getString(R.string.proximity)
        chartTitle = getString(R.string.proximity)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDataChanged(event: SensorEvent) {
        val value = event.values[0]
        mainChartXValueInfoLabel.text = getString(R.string.distanceChartLabel, value.toString())

        lineData.apply {
            addEntry(Entry(getDataSetByIndex(0).entryCount.toFloat(), value), 0)

            notifyDataChanged()
            mainChart.notifyDataSetChanged()
            mainChart.setVisibleXRangeMaximum(100F)
            mainChart.moveViewTo(entryCount.toFloat(), 0F, YAxis.AxisDependency.RIGHT)
        }
    }
}