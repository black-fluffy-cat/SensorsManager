package com.fluffycat.sensorsmanager.fragments

import android.graphics.Color
import android.hardware.SensorEvent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.fluffycat.sensorsmanager.converter.ValuesConverter
import com.fluffycat.sensorsmanager.sensors.ISensorController
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

abstract class BaseChartFragment : Fragment() {

    protected abstract val fragmentTitle: String
    protected abstract val chartTitle: String
    protected abstract var sensorController: ISensorController
    protected abstract val layoutResource: Int

    protected val valuesConverter = ValuesConverter()
    protected val lineData: LineData = createLineData()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setActivityTitle()
        return inflater.inflate(layoutResource, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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