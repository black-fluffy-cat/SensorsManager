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

class AccelerometerListener(private val accelerometerChartView: LineChart) : SensorEventListener {

    private var lineData: LineData

    init {
        //Create lineDataSets for 3 axis from accelerometer
        val lineDataSet1: LineDataSet = createDataSet(Color.GREEN, "Accelerometer X")
        val lineDataSet2: LineDataSet = createDataSet(Color.RED, "Y")
        val lineDataSet3: LineDataSet = createDataSet(Color.BLUE, "Z")
        lineData = LineData(lineDataSet1, lineDataSet2, lineDataSet3)

        accelerometerChartView.data = lineData
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
            accelerometerChartView.notifyDataSetChanged()
            accelerometerChartView.setVisibleXRangeMaximum(500F)
            accelerometerChartView.moveViewTo(entryCount.toFloat(), 0F, YAxis.AxisDependency.RIGHT)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // empty
    }
}