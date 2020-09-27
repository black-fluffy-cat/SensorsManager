package com.fluffycat.sensorsmanager.fragments

import android.hardware.SensorEvent
import android.os.Bundle
import android.view.View
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.sensors.GyroscopeSensorController
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import kotlinx.android.synthetic.main.gyroscope_fragment.*

class GyroscopeFragment : BaseChartFragment() {

    override val layoutResource = R.layout.gyroscope_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sensorController = GyroscopeSensorController(sensorManager)
        gyroscopeChart.data = lineData
        gyroscopeSensorInfoLabel.text = sensorController.getSensorInfo()
        fragmentTitle = getString(R.string.gyroscope)
        chartTitle = getString(R.string.gyroscope)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDataChanged(event: SensorEvent) {
        val convertedValues = event.values.copyOf().apply {
            forEachIndexed { index, element -> this[index] = valuesConverter.convertAngleValueToChosenUnit(element) }
        }

        val roundedValues = convertedValues.copyOf().apply {
            forEachIndexed { index, element -> this[index] = valuesConverter.roundValue(element) }
        }

        val labelTexts = mutableListOf<String>().apply {
            roundedValues.forEach { this.add(valuesConverter.convertAngularVelocityValueToStringWithSymbol(it)) }
        }

        gyroscopeXValueInfoLabel.text = getString(R.string.xChartLabel, labelTexts[0])
        gyroscopeYValueInfoLabel.text = getString(R.string.yChartLabel, labelTexts[1])
        gyroscopeZValueInfoLabel.text = getString(R.string.zChartLabel, labelTexts[2])

        lineData.apply {
            convertedValues.sliceArray(IntRange(0, 2)).forEachIndexed { index, value ->
                addEntry(Entry(getDataSetByIndex(index).entryCount.toFloat(), value), index)
            }

            notifyDataChanged()
            gyroscopeChart.notifyDataSetChanged()
            gyroscopeChart.setVisibleXRangeMaximum(500F)
            gyroscopeChart.moveViewTo(entryCount.toFloat(), 0F, YAxis.AxisDependency.RIGHT)
        }
    }
}