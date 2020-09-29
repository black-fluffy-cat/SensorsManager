package com.fluffycat.sensorsmanager.fragments

import android.hardware.SensorEvent
import android.os.Bundle
import android.view.View
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.sensors.AccelerometerSensorController
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import kotlinx.android.synthetic.main.chart_fragment.*

class AccelerometerFragment : BaseChartFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sensorController = AccelerometerSensorController(sensorManager)
        fragmentTitle = getString(R.string.accelerometer)
        chartTitle = getString(R.string.accelerometer)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDataChanged(event: SensorEvent) {
        val convertedValues = event.values.copyOf().apply {
            forEachIndexed { index, element -> this[index] = valuesConverter.convertDistanceValueToChosenUnit(element) }
        }

        val roundedValues = convertedValues.copyOf().apply {
            forEachIndexed { index, element -> this[index] = valuesConverter.roundValue(element) }
        }

        val labelTexts = mutableListOf<String>().apply {
            roundedValues.forEach { this.add(valuesConverter.convertAccelerationValueToStringWithSymbol(it)) }
        }

        mainChartXValueInfoLabel.text = getString(R.string.xChartLabel, labelTexts[0])
        mainChartYValueInfoLabel.text = getString(R.string.yChartLabel, labelTexts[1])
        mainChartZValueInfoLabel.text = getString(R.string.zChartLabel, labelTexts[2])

        lineData.apply {
            convertedValues.sliceArray(IntRange(0, 2)).forEachIndexed { index, value ->
                addEntry(Entry(getDataSetByIndex(index).entryCount.toFloat(), value), index)
            }

            notifyDataChanged()
            mainChart.notifyDataSetChanged()
            mainChart.setVisibleXRangeMaximum(500F)
            mainChart.moveViewTo(entryCount.toFloat(), 0F, YAxis.AxisDependency.RIGHT)
        }
    }
}
