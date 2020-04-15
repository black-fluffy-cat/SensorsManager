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

abstract class ThreeAxisSensorListener(private val threeAxisChartView: LineChart,
                                       chartTitle: String = "") : SensorEventListener {

    private var lineData: LineData

    init {
        // Create lineDataSets for 3 axis from sensor
        val lineDataSet1: LineDataSet = createDataSet(Color.GREEN, "$chartTitle X")
        val lineDataSet2: LineDataSet = createDataSet(Color.RED, "Y")
        val lineDataSet3: LineDataSet = createDataSet(Color.BLUE, "Z")
        lineData = LineData(lineDataSet1, lineDataSet2, lineDataSet3)

        threeAxisChartView.data = lineData
    }

    private fun createDataSet(dataSetColor: Int, label: String) = LineDataSet(ArrayList(), label).apply {
        setDrawCircles(false)
        lineWidth = 3.1f
        color = dataSetColor
    }

    override fun onSensorChanged(event: SensorEvent) {
        Log.d(tag, "onSensorChanged")
        lineData.apply {
            addEntry(Entry(getDataSetByIndex(0).entryCount.toFloat(), event.values[0]), 0)
            addEntry(Entry(getDataSetByIndex(1).entryCount.toFloat(), event.values[1]), 1)
            addEntry(Entry(getDataSetByIndex(2).entryCount.toFloat(), event.values[2]), 2)

            notifyDataChanged()
            threeAxisChartView.notifyDataSetChanged()
            threeAxisChartView.setVisibleXRangeMaximum(500F)
            threeAxisChartView.moveViewTo(entryCount.toFloat(), 0F, YAxis.AxisDependency.RIGHT)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // empty
    }
}