package com.fluffycat.sensorsmanager.fragments

import android.hardware.SensorEvent
import android.os.Bundle
import android.view.View
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.SensorsManagerApplication
import com.fluffycat.sensorsmanager.sensors.ISensorController
import com.fluffycat.sensorsmanager.sensors.LinearAccelerationSensorController
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import kotlinx.android.synthetic.main.linear_acceleration_fragment.*

class LinearAccelerationFragment : BaseChartFragment() {

    override val fragmentTitle = getString(R.string.linearAcceleration)
    override val chartTitle = getString(R.string.linearAcceleration)
    override val layoutResource = R.layout.linear_acceleration_fragment
    override var sensorController: ISensorController =
        LinearAccelerationSensorController(context ?: SensorsManagerApplication.getContext())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linearAccelerationChart.data = lineData
        linearAccelerationChart.description = Description().apply { text = "" }
        linearAccelerationSensorInfoLabel.text = sensorController.getSensorInfo()
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

        linearAccelerationXValueInfoLabel.text = "X: ${labelTexts[0]}"
        linearAccelerationYValueInfoLabel.text = "Y: ${labelTexts[1]}"
        linearAccelerationZValueInfoLabel.text = "Z: ${labelTexts[2]}"

        lineData.apply {
            convertedValues.sliceArray(IntRange(0, 2)).forEachIndexed { index, value ->
                addEntry(Entry(getDataSetByIndex(index).entryCount.toFloat(), value), index)
            }
            notifyDataChanged()
        }

        linearAccelerationChart.apply {
            notifyDataSetChanged()
            setVisibleXRangeMaximum(500F)
            moveViewTo(lineData.entryCount.toFloat(), 0F, YAxis.AxisDependency.RIGHT)
        }
    }
}