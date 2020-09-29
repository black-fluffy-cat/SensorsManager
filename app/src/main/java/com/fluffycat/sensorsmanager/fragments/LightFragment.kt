package com.fluffycat.sensorsmanager.fragments

import android.graphics.Color
import android.hardware.SensorEvent
import android.os.Bundle
import android.view.View
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.sensors.LightSensorController
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import kotlinx.android.synthetic.main.chart_fragment.*

class LightFragment : BaseChartFragment() {

    override fun createLineData() = LineData(createDataSet(Color.YELLOW, chartTitle))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sensorController = LightSensorController(sensorManager)
        fragmentTitle = getString(R.string.light)
        chartTitle = "Light power"
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDataChanged(event: SensorEvent) {
        val value = event.values[0]
        mainChartXValueInfoLabel.text = getString(R.string.lightChartLabel, value.toString())

        lineData.apply {
            addEntry(Entry(getDataSetByIndex(0).entryCount.toFloat(), value), 0)

            notifyDataChanged()
            mainChart.notifyDataSetChanged()
            mainChart.setVisibleXRangeMaximum(100F)
            mainChart.moveViewTo(entryCount.toFloat(), 0F, YAxis.AxisDependency.RIGHT)
        }
    }
}