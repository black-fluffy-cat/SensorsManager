package com.fluffycat.sensorsmanager.fragments

import android.hardware.SensorEvent
import android.os.Bundle
import android.view.View
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.SensorsManagerApplication
import com.fluffycat.sensorsmanager.sensors.ISensorController
import com.fluffycat.sensorsmanager.sensors.MagneticFieldSensorController
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import kotlinx.android.synthetic.main.magnetic_field_fragment.*

class MagneticFieldFragment : BaseChartFragment() {

    override val fragmentTitle = getString(R.string.magnetic_field)
    override val chartTitle = getString(R.string.magnetic_field)
    override val layoutResource = R.layout.magnetic_field_fragment
    override var sensorController: ISensorController =
        MagneticFieldSensorController(context ?: SensorsManagerApplication.getContext())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        magneticFieldChart.data = lineData
        magneticFieldSensorInfoLabel.text = sensorController.getSensorInfo()
    }

    override fun onDataChanged(event: SensorEvent) {
        val roundedValues = event.values.copyOf().apply {
            forEach { valuesConverter.roundValue(it) }
        }

        val convertedValues = roundedValues.copyOf().apply {
            forEach { valuesConverter.convertMagneticFieldValueToStringWithSymbol(it) }
        }

        val labelTexts = roundedValues.copyOf().apply {
            forEach { valuesConverter.convertMagneticFieldValueToStringWithSymbol(it) }
        }

        magneticFieldXValueInfoLabel.text = "X: ${labelTexts[0]}"
        magneticFieldYValueInfoLabel.text = "Y: ${labelTexts[1]}"
        magneticFieldZValueInfoLabel.text = "Z: ${labelTexts[2]}"

        lineData.apply {
            convertedValues.sliceArray(IntRange(0, 2)).forEachIndexed { index, value ->
                addEntry(Entry(getDataSetByIndex(index).entryCount.toFloat(), value), index)
            }

            notifyDataChanged()
            magneticFieldChart.notifyDataSetChanged()
            magneticFieldChart.setVisibleXRangeMaximum(500F)
            magneticFieldChart.moveViewTo(entryCount.toFloat(), 0F, YAxis.AxisDependency.RIGHT)
        }
    }
}