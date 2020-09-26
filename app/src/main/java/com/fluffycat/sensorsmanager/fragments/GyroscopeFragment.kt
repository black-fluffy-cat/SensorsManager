package com.fluffycat.sensorsmanager.fragments

import android.hardware.SensorEvent
import android.os.Bundle
import android.view.View
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.SensorsManagerApplication
import com.fluffycat.sensorsmanager.sensors.GyroscopeSensorController
import com.fluffycat.sensorsmanager.sensors.ISensorController
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import kotlinx.android.synthetic.main.gyroscope_fragment.*

class GyroscopeFragment : BaseChartFragment() {

    override val fragmentTitle = getString(R.string.gyroscope)
    override val chartTitle = getString(R.string.gyroscope)
    override val layoutResource = R.layout.gyroscope_fragment
    override var sensorController: ISensorController =
        GyroscopeSensorController(context ?: SensorsManagerApplication.getContext())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gyroscopeChart.data = lineData
        gyroscopeSensorInfoLabel.text = sensorController.getSensorInfo()
    }

    override fun onDataChanged(event: SensorEvent) {
        val roundedValues = event.values.copyOf().apply {
            forEach { valuesConverter.roundValue(it) }
        }

        val convertedValues = roundedValues.copyOf().apply {
            forEach { valuesConverter.convertAngleValueToChosenUnit(it) }
        }
        val labelTexts = roundedValues.copyOf().apply {
            forEach { valuesConverter.convertAngularVelocityValueToStringWithSymbol(it) }
        }

        gyroscopeXValueInfoLabel.text = "X: ${labelTexts[0]}"
        gyroscopeYValueInfoLabel.text = "Y: ${labelTexts[1]}"
        gyroscopeZValueInfoLabel.text = "Z: ${labelTexts[2]}"

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