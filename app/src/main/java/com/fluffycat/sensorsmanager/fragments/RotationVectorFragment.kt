package com.fluffycat.sensorsmanager.fragments

import android.hardware.SensorEvent
import android.os.Bundle
import android.view.View
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.sensors.RotationVectorController
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import kotlinx.android.synthetic.main.rotation_vector_fragment.*

class RotationVectorFragment : BaseChartFragment() {

    override val layoutResource = R.layout.rotation_vector_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sensorController = RotationVectorController(sensorManager)
        rotationVectorChart.data = lineData
        rotationVectorSensorInfoLabel.text = sensorController.getSensorInfo()
        fragmentTitle = getString(R.string.rotationVector)
        chartTitle = getString(R.string.rotationVector)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDataChanged(event: SensorEvent) {
        val roundedValues = event.values.copyOf().apply {
            forEach { valuesConverter.roundValue(it) }
        }

        rotationVectorXValueInfoLabel.text = getString(R.string.xChartLabel, roundedValues[0])
        rotationVectorYValueInfoLabel.text = getString(R.string.yChartLabel, roundedValues[1])
        rotationVectorZValueInfoLabel.text = getString(R.string.zChartLabel, roundedValues[2])

        lineData.apply {
            roundedValues.sliceArray(IntRange(0, 2)).forEachIndexed { index, value ->
                addEntry(Entry(getDataSetByIndex(index).entryCount.toFloat(), value), index)
            }

            notifyDataChanged()
            rotationVectorChart.notifyDataSetChanged()
            rotationVectorChart.setVisibleXRangeMaximum(500F)
            rotationVectorChart.moveViewTo(entryCount.toFloat(), 0F, YAxis.AxisDependency.RIGHT)
        }
    }
}