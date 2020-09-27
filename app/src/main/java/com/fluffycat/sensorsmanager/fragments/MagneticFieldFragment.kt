package com.fluffycat.sensorsmanager.fragments

import android.hardware.SensorEvent
import android.os.Bundle
import android.view.View
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.sensors.MagneticFieldSensorController
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import kotlinx.android.synthetic.main.magnetic_field_fragment.*

class MagneticFieldFragment : BaseChartFragment() {

    override val layoutResource = R.layout.magnetic_field_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sensorController = MagneticFieldSensorController(sensorManager)
        magneticFieldChart.data = lineData
        magneticFieldSensorInfoLabel.text = sensorController.getSensorInfo()
        fragmentTitle = getString(R.string.magnetic_field)
        chartTitle = getString(R.string.magnetic_field)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDataChanged(event: SensorEvent) {
        val roundedValues = event.values.copyOf().apply {
            forEachIndexed { index, element -> this[index] = valuesConverter.roundValue(element) }
        }

        val labelTexts = mutableListOf<String>().apply {
            roundedValues.forEach { this.add(valuesConverter.convertMagneticFieldValueToStringWithSymbol(it)) }
        }

        magneticFieldXValueInfoLabel.text = getString(R.string.xChartLabel, labelTexts[0])
        magneticFieldYValueInfoLabel.text = getString(R.string.yChartLabel, labelTexts[1])
        magneticFieldZValueInfoLabel.text = getString(R.string.zChartLabel, labelTexts[2])

        lineData.apply {
            roundedValues.sliceArray(IntRange(0, 2)).forEachIndexed { index, value ->
                addEntry(Entry(getDataSetByIndex(index).entryCount.toFloat(), value), index)
            }

            notifyDataChanged()
            magneticFieldChart.notifyDataSetChanged()
            magneticFieldChart.setVisibleXRangeMaximum(500F)
            magneticFieldChart.moveViewTo(entryCount.toFloat(), 0F, YAxis.AxisDependency.RIGHT)
        }
    }
}