package com.fluffycat.sensorsmanager.listeners

import com.github.mikephil.charting.charts.LineChart

class LightSensorListener(magneticFieldChartView: LineChart) :
    OneAxisSensorListener(magneticFieldChartView, chartTitle) {

    companion object {
        const val chartTitle = "Light power"
    }
}