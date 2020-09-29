package com.fluffycat.sensorsmanager.fragments

import android.hardware.SensorEvent
import android.os.Bundle
import android.view.View
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.sensors.MagneticFieldSensorController
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import kotlinx.android.synthetic.main.chart_fragment.*

class MagneticFieldFragment : BaseChartFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sensorController = MagneticFieldSensorController(sensorManager)
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

        mainChartXValueInfoLabel.text = getString(R.string.xChartLabel, labelTexts[0])
        mainChartYValueInfoLabel.text = getString(R.string.yChartLabel, labelTexts[1])
        mainChartZValueInfoLabel.text = getString(R.string.zChartLabel, labelTexts[2])

        lineData.apply {
            roundedValues.sliceArray(IntRange(0, 2)).forEachIndexed { index, value ->
                addEntry(Entry(getDataSetByIndex(index).entryCount.toFloat(), value), index)
            }

            notifyDataChanged()
            mainChart.notifyDataSetChanged()
            mainChart.setVisibleXRangeMaximum(500F)
            mainChart.moveViewTo(entryCount.toFloat(), 0F, YAxis.AxisDependency.RIGHT)
        }
    }
}