package com.fluffycat.sensorsmanager.fragments

import android.hardware.SensorEvent
import android.os.Bundle
import android.view.View
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.sensors.AccelerometerSensorController
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import kotlinx.android.synthetic.main.accelerometer_fragment.*

class AccelerometerFragment : BaseChartFragment() {

    override val layoutResource = R.layout.accelerometer_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sensorController = AccelerometerSensorController(sensorManager)
        accelerometerChart.data = lineData
        accelerometerChart.description = Description().apply { text = "" }
        accelerometerSensorInfoLabel.text = sensorController.getSensorInfo()

        fragmentTitle = getString(R.string.accelerometer)
        chartTitle = getString(R.string.accelerometer)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDataChanged(event: SensorEvent) {
        val roundedValues = event.values.copyOf().apply {
            forEach { valuesConverter.roundValue(it) }
        }

        val convertedValues = roundedValues.copyOf().apply {
            forEach { valuesConverter.convertDistanceValueToChosenUnit(it) }
        }

        val labelTexts = roundedValues.copyOf().apply {
            forEach { valuesConverter.convertAccelerationValueToStringWithSymbol(it) }
        }

        accelerometerXValueInfoLabel.text = getString(R.string.xChartLabel, labelTexts[0])
        accelerometerYValueInfoLabel.text = getString(R.string.yChartLabel, labelTexts[1])
        accelerometerZValueInfoLabel.text = getString(R.string.zChartLabel, labelTexts[2])

        lineData.apply {
            convertedValues.sliceArray(IntRange(0, 2)).forEachIndexed { index, value ->
                addEntry(Entry(getDataSetByIndex(index).entryCount.toFloat(), value), index)
            }

            notifyDataChanged()
            accelerometerChart.notifyDataSetChanged()
            accelerometerChart.setVisibleXRangeMaximum(500F)
            accelerometerChart.moveViewTo(entryCount.toFloat(), 0F, YAxis.AxisDependency.RIGHT)
        }
    }
}
