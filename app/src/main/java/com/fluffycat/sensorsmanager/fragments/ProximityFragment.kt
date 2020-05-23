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
import com.fluffycat.sensorsmanager.sensors.ProximitySensorController
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.proximity_fragment.*
import java.util.*

class ProximityFragment : Fragment() {

    companion object {
        const val TAG = "ProximityFragmentTAG"
        const val chartTitle = "Proximity power"
    }

    private lateinit var proximitySensorController: ISensorController
    private var lineData: LineData

    init {
        val lineDataSet1: LineDataSet = createDataSet(Color.CYAN, chartTitle)
        lineData = LineData(lineDataSet1)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setActivityTitle()
        return inflater.inflate(R.layout.proximity_fragment, container, false)
    }

    private fun setActivityTitle() {
        activity?.title = getString(R.string.proximity)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        proximitySensorController = ProximitySensorController(context ?: SensorsManagerApplication.getContext())

        // TODO Can I observe it here and forget about it?
        proximitySensorController.sensorCurrentData.observe(this, Observer { sensorEvent ->
            onDataChanged(sensorEvent)
        })

        proximityChart.data = lineData
        proximitySensorInfoLabel.text = proximitySensorController.getSensorInfo()
    }

    @Suppress("SameParameterValue")
    private fun createDataSet(dataSetColor: Int, label: String) = LineDataSet(ArrayList(), label).apply {
        setDrawCircles(false)
        lineWidth = 3.1f
        color = dataSetColor
    }

    override fun onStart() {
        super.onStart()
        proximitySensorController.startReceivingData()
    }

    override fun onStop() {
        super.onStop()
        proximitySensorController.stopReceivingData()
    }

    private fun onDataChanged(event: SensorEvent) {
        val value = event.values[0]
        proximityXValueInfoLabel.text = "Distance: $value cm"

        lineData.apply {
            addEntry(Entry(getDataSetByIndex(0).entryCount.toFloat(), value), 0)

            notifyDataChanged()
            proximityChart.notifyDataSetChanged()
            proximityChart.setVisibleXRangeMaximum(100F)
            proximityChart.moveViewTo(entryCount.toFloat(), 0F, YAxis.AxisDependency.RIGHT)
        }
    }
}