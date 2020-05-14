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
import com.fluffycat.sensorsmanager.sensors.AccelerometerSensorController
import com.fluffycat.sensorsmanager.sensors.ISensorController
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

    init {
        // Create lineDataSets for 3 axis from sensor
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
        lineData.apply {
            addEntry(Entry(getDataSetByIndex(0).entryCount.toFloat(), event.values[0]), 0)
            addEntry(Entry(getDataSetByIndex(1).entryCount.toFloat(), event.values[1]), 1)
            addEntry(Entry(getDataSetByIndex(2).entryCount.toFloat(), event.values[2]), 2)

            notifyDataChanged()
            accelerometerChart.notifyDataSetChanged()
            accelerometerChart.setVisibleXRangeMaximum(500F)
            accelerometerChart.moveViewTo(entryCount.toFloat(), 0F, YAxis.AxisDependency.RIGHT)
        }
    }
}