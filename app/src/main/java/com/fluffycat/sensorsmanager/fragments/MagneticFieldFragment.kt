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
import com.fluffycat.sensorsmanager.sensors.ISensorController
import com.fluffycat.sensorsmanager.sensors.MagneticFieldSensorController
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.magnetic_field_fragment.*

class MagneticFieldFragment : Fragment() {

    companion object {
        const val TAG = "MagneticFieldTAG"
        const val chartTitle = "Magnetic field"
    }

    private lateinit var magneticFieldSensorController: ISensorController
    private var lineData: LineData

    init {
        val lineDataSet1: LineDataSet = createDataSet(Color.GREEN, "$chartTitle X")
        val lineDataSet2: LineDataSet = createDataSet(Color.RED, "Y")
        val lineDataSet3: LineDataSet = createDataSet(Color.BLUE, "Z")
        lineData = LineData(lineDataSet1, lineDataSet2, lineDataSet3)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setActivityTitle()
        return inflater.inflate(R.layout.magnetic_field_fragment, container, false)
    }

    private fun setActivityTitle() {
        activity?.title = getString(R.string.magnetic_field)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        magneticFieldSensorController =
            MagneticFieldSensorController(context ?: SensorsManagerApplication.getContext())

        // TODO Can I observe it here and forget about it?
        magneticFieldSensorController.sensorCurrentData.observe(this, Observer { sensorEvent ->
            onDataChanged(sensorEvent)
        })

        magneticFieldChart.data = lineData
        magneticFieldSensorInfoLabel.text = magneticFieldSensorController.getSensorInfo()
    }

    private fun createDataSet(dataSetColor: Int, label: String) = LineDataSet(ArrayList(), label).apply {
        setDrawCircles(false)
        lineWidth = 3.1f
        color = dataSetColor
    }

    override fun onStart() {
        super.onStart()
        magneticFieldSensorController.startReceivingData()
    }

    override fun onStop() {
        super.onStop()
        magneticFieldSensorController.stopReceivingData()
    }

    private fun onDataChanged(event: SensorEvent) {
        lineData.apply {
            addEntry(Entry(getDataSetByIndex(0).entryCount.toFloat(), event.values[0]), 0)
            addEntry(Entry(getDataSetByIndex(1).entryCount.toFloat(), event.values[1]), 1)
            addEntry(Entry(getDataSetByIndex(2).entryCount.toFloat(), event.values[2]), 2)

            notifyDataChanged()
            magneticFieldChart.notifyDataSetChanged()
            magneticFieldChart.setVisibleXRangeMaximum(500F)
            magneticFieldChart.moveViewTo(entryCount.toFloat(), 0F, YAxis.AxisDependency.RIGHT)
        }
    }
}