package com.fluffycat.sensorsmanager.fragments

import android.graphics.Color
import android.hardware.SensorEvent
import android.os.Bundle
import android.view.View
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.SensorsManagerApplication
import com.fluffycat.sensorsmanager.sensors.ISensorController
import com.fluffycat.sensorsmanager.sensors.LightSensorController
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import kotlinx.android.synthetic.main.light_fragment.*

class LightFragment : BaseChartFragment() {

    override val fragmentTitle = getString(R.string.light)
    override val chartTitle = "Light power"
    override val layoutResource = R.layout.light_fragment
    override var sensorController: ISensorController =
        LightSensorController(context ?: SensorsManagerApplication.getContext())

    override fun createLineData() = LineData(createDataSet(Color.YELLOW, chartTitle))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lightChart.data = lineData
        lightSensorInfoLabel.text = sensorController.getSensorInfo()
    }

    override fun onDataChanged(event: SensorEvent) {
        val value = event.values[0]
        lightXValueInfoLabel.text = "Lux: $value"

        lineData.apply {
            addEntry(Entry(getDataSetByIndex(0).entryCount.toFloat(), value), 0)

            notifyDataChanged()
            lightChart.notifyDataSetChanged()
            lightChart.setVisibleXRangeMaximum(100F)
            lightChart.moveViewTo(entryCount.toFloat(), 0F, YAxis.AxisDependency.RIGHT)
        }
    }
}