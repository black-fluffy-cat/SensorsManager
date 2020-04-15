package com.fluffycat.sensorsmanager.listeners

import com.github.mikephil.charting.charts.LineChart

class MagneticFieldListener (magneticFieldChartView: LineChart) :
    ThreeAxisSensorListener(magneticFieldChartView, chartTitle) {

    companion object {
        const val chartTitle = "Magnetic field"
    }
}