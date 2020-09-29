package com.fluffycat.sensorsmanager.fragments

import android.content.Context
import android.graphics.Color
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.converter.ValuesConverter
import com.fluffycat.sensorsmanager.sensors.ISensorController
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.chart_fragment.*

abstract class BaseChartFragment : Fragment() {

    protected var fragmentTitle: String = ""
    protected var chartTitle: String = ""
    protected lateinit var sensorController: ISensorController
    private val layoutResource = R.layout.chart_fragment

    protected var sensorManager: SensorManager? = null

    protected val valuesConverter = ValuesConverter()
    protected lateinit var lineData: LineData

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        lineData = createLineData()
        sensorManager = context?.getSystemService(Context.SENSOR_SERVICE) as SensorManager?
        Log.d("ABAB", "context: $context, smanager: $sensorManager")
        return inflater.inflate(layoutResource, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActivityTitle()
        mainChart.data = lineData
        mainChart.description = Description().apply { text = "" }
        mainChartSensorInfoLabel.text = sensorController.getSensorInfo()

        // TODO Can I observe it here and forget about it?
        sensorController.sensorCurrentData.observe(this, Observer { sensorEvent ->
            onDataChanged(sensorEvent)
        })
    }

    protected abstract fun onDataChanged(event: SensorEvent)

    override fun onStart() {
        super.onStart()
        sensorController.startReceivingData()
    }

    override fun onStop() {
        super.onStop()
        sensorController.stopReceivingData()
    }

    protected open fun createLineData(): LineData {
        val lineDataSet1 = createDataSet(Color.GREEN, "$chartTitle X")
        val lineDataSet2 = createDataSet(Color.RED, "Y")
        val lineDataSet3 = createDataSet(Color.BLUE, "Z")
        return LineData(lineDataSet1, lineDataSet2, lineDataSet3)
    }

    private fun setActivityTitle() {
        activity?.title = fragmentTitle
    }

    protected open fun createDataSet(dataSetColor: Int, label: String) = LineDataSet(ArrayList(), label).apply {
        setDrawCircles(false)
        lineWidth = 3.1f
        color = dataSetColor
    }
}