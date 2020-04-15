package com.fluffycat.sensorsmanager.listeners

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.util.Log
import com.fluffycat.sensorsmanager.utils.tag
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.util.*

abstract class OneAxisSensorListener(private val oneAxisChartView: LineChart,
                            chartTitle: String = "") : SensorEventListener {

    private var lineData: LineData

    init {
        val lineDataSet1: LineDataSet = createDataSet(Color.CYAN, chartTitle)
        lineData = LineData(lineDataSet1)
        oneAxisChartView.data = lineData
    }

    @Suppress("SameParameterValue")
    private fun createDataSet(dataSetColor: Int, label: String) = LineDataSet(ArrayList(), label).apply {
        setDrawCircles(false)
        lineWidth = 3.1f
        color = dataSetColor
    }

    override fun onSensorChanged(event: SensorEvent) {
        Log.d(tag, "onSensorChanged")
        lineData.apply {
            addEntry(Entry(getDataSetByIndex(0).entryCount.toFloat(), event.values[0]), 0)

            notifyDataChanged()
            oneAxisChartView.notifyDataSetChanged()
            oneAxisChartView.setVisibleXRangeMaximum(100F)
            oneAxisChartView.moveViewTo(entryCount.toFloat(), 0F, YAxis.AxisDependency.RIGHT)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // empty
    }
}