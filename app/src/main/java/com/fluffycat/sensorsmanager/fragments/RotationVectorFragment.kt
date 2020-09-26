package com.fluffycat.sensorsmanager.fragments

import android.hardware.SensorEvent
import android.os.Bundle
import android.view.View
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.SensorsManagerApplication
import com.fluffycat.sensorsmanager.sensors.ISensorController
import com.fluffycat.sensorsmanager.sensors.RotationVectorController
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import kotlinx.android.synthetic.main.rotation_vector_fragment.*

class RotationVectorFragment : BaseChartFragment() {

    override val fragmentTitle = getString(R.string.rotationVector)
    override val chartTitle = getString(R.string.rotationVector)
    override val layoutResource = R.layout.rotation_vector_fragment
    override var sensorController: ISensorController =
        RotationVectorController(context ?: SensorsManagerApplication.getContext())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rotationVectorChart.data = lineData
        rotationVectorSensorInfoLabel.text = sensorController.getSensorInfo()
    }

    override fun onDataChanged(event: SensorEvent) {
        val roundedValues = event.values.copyOf().apply {
            forEach { valuesConverter.roundValue(it) }
        }

        val xLabelText = "X: ${roundedValues[0]}"
        val yLabelText = "Y: ${roundedValues[1]}"
        val zLabelText = "Z: ${roundedValues[2]}"

        rotationVectorXValueInfoLabel.text = xLabelText
        rotationVectorYValueInfoLabel.text = yLabelText
        rotationVectorZValueInfoLabel.text = zLabelText

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