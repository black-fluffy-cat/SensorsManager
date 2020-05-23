package com.fluffycat.sensorsmanager.fragments

import android.graphics.Color
import android.hardware.SensorEvent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.SensorsManagerApplication
import com.fluffycat.sensorsmanager.converter.ValuesConverter
import com.fluffycat.sensorsmanager.sensors.ISensorController
import com.fluffycat.sensorsmanager.sensors.RotationVectorController
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.rotation_vector_fragment.*

class RotationVectorFragment : Fragment() {

    companion object {
        const val TAG = "RotationVectorFragmentTAG"
        const val chartTitle = "Rotation vector"
    }

    private lateinit var rotationVectorController: ISensorController
    private var lineData: LineData
    private val valuesConverter: ValuesConverter = ValuesConverter()

    init {
        val lineDataSet1: LineDataSet = createDataSet(Color.GREEN, "$chartTitle X")
        val lineDataSet2: LineDataSet = createDataSet(Color.RED, "Y")
        val lineDataSet3: LineDataSet = createDataSet(Color.BLUE, "Z")
        lineData = LineData(lineDataSet1, lineDataSet2, lineDataSet3)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setActivityTitle()
        return inflater.inflate(R.layout.rotation_vector_fragment, container, false)
    }

    private fun setActivityTitle() {
        activity?.title = getString(R.string.rotationVector)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rotationVectorController = RotationVectorController(context ?: SensorsManagerApplication.getContext())

        // TODO Can I observe it here and forget about it?
        rotationVectorController.sensorCurrentData.observe(this, Observer { sensorEvent ->
            onDataChanged(sensorEvent)
        })

        rotationVectorChart.data = lineData
        rotationVectorSensorInfoLabel.text = rotationVectorController.getSensorInfo()
    }

    private fun createDataSet(dataSetColor: Int, label: String) = LineDataSet(ArrayList(), label).apply {
        setDrawCircles(false)
        lineWidth = 3.1f
        color = dataSetColor
    }

    override fun onStart() {
        super.onStart()
        rotationVectorController.startReceivingData()
    }

    override fun onStop() {
        super.onStop()
        rotationVectorController.stopReceivingData()
    }

    private fun onDataChanged(event: SensorEvent) {
        val xValue = valuesConverter.roundValue(event.values[0])
        val yValue = valuesConverter.roundValue(event.values[1])
        val zValue = valuesConverter.roundValue(event.values[2])


        val xLabelText = "X: $xValue"
        val yLabelText = "Y: $yValue"
        val zLabelText = "Z: $zValue"

        rotationVectorXValueInfoLabel.text = xLabelText
        rotationVectorYValueInfoLabel.text = yLabelText
        rotationVectorZValueInfoLabel.text = zLabelText

        lineData.apply {
            addEntry(Entry(getDataSetByIndex(0).entryCount.toFloat(), xValue), 0)
            addEntry(Entry(getDataSetByIndex(1).entryCount.toFloat(), yValue), 1)
            addEntry(Entry(getDataSetByIndex(2).entryCount.toFloat(), zValue), 2)

            notifyDataChanged()
            rotationVectorChart.notifyDataSetChanged()
            rotationVectorChart.setVisibleXRangeMaximum(500F)
            rotationVectorChart.moveViewTo(entryCount.toFloat(), 0F, YAxis.AxisDependency.RIGHT)
        }
    }
}