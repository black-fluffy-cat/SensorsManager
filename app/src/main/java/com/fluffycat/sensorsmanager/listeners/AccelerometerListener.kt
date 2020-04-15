package com.fluffycat.sensorsmanager.listeners

import com.github.mikephil.charting.charts.LineChart

class AccelerometerListener(accelerometerChartView: LineChart) :
    ThreeAxisSensorListener(accelerometerChartView, chartTitle) {

    companion object {
        const val chartTitle = "Accelerometer"
    }
}