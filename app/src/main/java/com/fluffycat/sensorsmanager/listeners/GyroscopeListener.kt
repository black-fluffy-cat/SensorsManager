package com.fluffycat.sensorsmanager.listeners

import com.github.mikephil.charting.charts.LineChart

class GyroscopeListener (gyroscopeChartView: LineChart) :
    ThreeAxisSensorListener(gyroscopeChartView, chartTitle) {

    companion object {
        const val chartTitle = "Gyroscope"
    }
}