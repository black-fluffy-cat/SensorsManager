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
import com.fluffycat.sensorsmanager.sensors.AccelerometerSensorController
import com.fluffycat.sensorsmanager.sensors.ISensorController
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.accelerometer_fragment.*

class AccelerometerFragment : Fragment() {

    companion object {
        const val TAG = "AccelerometerFragmentTAG"
        const val chartTitle = "Accelerometer"
    }

    private lateinit var accelerometerSensorController: ISensorController
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
        return inflater.inflate(R.layout.accelerometer_fragment, container, false)
    }

    private fun setActivityTitle() {
        activity?.title = getString(R.string.accelerometer)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accelerometerSensorController =
            AccelerometerSensorController(context ?: SensorsManagerApplication.getContext())

        // TODO Can I observe it here and forget about it?
        accelerometerSensorController.sensorCurrentData.observe(this, Observer { sensorEvent ->
            onDataChanged(sensorEvent)
        })

        accelerometerChart.data = lineData
        accelerometerChart.description = Description().apply { text = "" }
        accelerometerSensorInfoLabel.text = accelerometerSensorController.getSensorInfo()
    }

    private fun createDataSet(dataSetColor: Int, label: String) = LineDataSet(ArrayList(), label).apply {
        setDrawCircles(false)
        lineWidth = 3.1f
        color = dataSetColor
    }

    override fun onStart() {
        super.onStart()
        accelerometerSensorController.startReceivingData()
    }

    override fun onStop() {
        super.onStop()
        accelerometerSensorController.stopReceivingData()
    }

    private fun onDataChanged(event: SensorEvent) {
        val xValue = valuesConverter.roundValue(event.values[0])
        val yValue = valuesConverter.roundValue(event.values[1])
        val zValue = valuesConverter.roundValue(event.values[2])

        val convertedXValue = valuesConverter.convertDistanceValueToChosenUnit(xValue)
        val convertedYValue = valuesConverter.convertDistanceValueToChosenUnit(yValue)
        val convertedZValue = valuesConverter.convertDistanceValueToChosenUnit(zValue)

        val xLabelText = "X: ${valuesConverter.convertAccelerationValueToStringWithSymbol(xValue)}"
        val yLabelText = "Y: ${valuesConverter.convertAccelerationValueToStringWithSymbol(yValue)}"
        val zLabelText = "Z: ${valuesConverter.convertAccelerationValueToStringWithSymbol(zValue)}"

        accelerometerXValueInfoLabel.text = xLabelText
        accelerometerYValueInfoLabel.text = yLabelText
        accelerometerZValueInfoLabel.text = zLabelText

        lineData.apply {
            addEntry(Entry(getDataSetByIndex(0).entryCount.toFloat(), convertedXValue), 0)
            addEntry(Entry(getDataSetByIndex(1).entryCount.toFloat(), convertedYValue), 1)
            addEntry(Entry(getDataSetByIndex(2).entryCount.toFloat(), convertedZValue), 2)

            notifyDataChanged()
            accelerometerChart.notifyDataSetChanged()
            accelerometerChart.setVisibleXRangeMaximum(500F)
            accelerometerChart.moveViewTo(entryCount.toFloat(), 0F, YAxis.AxisDependency.RIGHT)
        }
    }
}