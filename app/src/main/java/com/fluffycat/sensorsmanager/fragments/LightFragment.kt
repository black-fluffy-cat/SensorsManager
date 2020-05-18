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
import com.fluffycat.sensorsmanager.sensors.LightSensorController
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.light_fragment.*
import java.util.*

class LightFragment : Fragment() {

    companion object {
        const val TAG = "LightFragmentTAG"
        const val chartTitle = "Light power"
    }

    private lateinit var lightSensorController: ISensorController
    private var lineData: LineData

    init {
        val lineDataSet1: LineDataSet = createDataSet(Color.CYAN, chartTitle)
        lineData = LineData(lineDataSet1)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setActivityTitle()
        return inflater.inflate(R.layout.light_fragment, container, false)
    }

    private fun setActivityTitle() {
        activity?.title = getString(R.string.light)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lightSensorController = LightSensorController(context ?: SensorsManagerApplication.getContext())

        // TODO Can I observe it here and forget about it?
        lightSensorController.sensorCurrentData.observe(this, Observer { sensorEvent ->
            onDataChanged(sensorEvent)
        })

        lightChart.data = lineData
        lightSensorInfoLabel.text = lightSensorController.getSensorInfo()
    }

    @Suppress("SameParameterValue")
    private fun createDataSet(dataSetColor: Int, label: String) = LineDataSet(ArrayList(), label).apply {
        setDrawCircles(false)
        lineWidth = 3.1f
        color = dataSetColor
    }

    override fun onStart() {
        super.onStart()
        lightSensorController.startReceivingData()
    }

    override fun onStop() {
        super.onStop()
        lightSensorController.stopReceivingData()
    }

    private fun onDataChanged(event: SensorEvent) {
        lineData.apply {
            addEntry(Entry(getDataSetByIndex(0).entryCount.toFloat(), event.values[0]), 0)

            notifyDataChanged()
            lightChart.notifyDataSetChanged()
            lightChart.setVisibleXRangeMaximum(100F)
            lightChart.moveViewTo(entryCount.toFloat(), 0F, YAxis.AxisDependency.RIGHT)
        }
    }
}