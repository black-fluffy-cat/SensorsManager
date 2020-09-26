package com.fluffycat.sensorsmanager.fragments

import android.hardware.SensorEvent
import android.os.Bundle
import android.view.View
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.SensorsManagerApplication
import com.fluffycat.sensorsmanager.sensors.AccelerometerSensorController
import com.fluffycat.sensorsmanager.sensors.ISensorController
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import kotlinx.android.synthetic.main.accelerometer_fragment.*

class AccelerometerFragment : BaseChartFragment() {

    override val fragmentTitle = getString(R.string.accelerometer)
    override val chartTitle = getString(R.string.accelerometer)
    override val layoutResource = R.layout.accelerometer_fragment
    override var sensorController: ISensorController =
        AccelerometerSensorController(context ?: SensorsManagerApplication.getContext())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accelerometerChart.data = lineData
        accelerometerChart.description = Description().apply { text = "" }
        accelerometerSensorInfoLabel.text = sensorController.getSensorInfo()
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

        accelerometerXValueInfoLabel.text = "X: ${labelTexts[0]}"
        accelerometerYValueInfoLabel.text = "Y: ${labelTexts[1]}"
        accelerometerZValueInfoLabel.text = "Z: ${labelTexts[2]}"

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
