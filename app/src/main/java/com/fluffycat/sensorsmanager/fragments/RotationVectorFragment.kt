package com.fluffycat.sensorsmanager.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.sensors.ROTATION_VECTOR_SENSOR_TYPE
import com.fluffycat.sensorsmanager.sensors.SensorController
import com.github.mikephil.charting.data.LineData

class RotationVectorFragment : BaseChartFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sensorController = SensorController(sensorManager, ROTATION_VECTOR_SENSOR_TYPE)
        fragmentTitle = getString(R.string.rotationVector)
        chartTitle = getString(R.string.rotationVector)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun createLineData(): LineData {
        val lineDataSet1 = createDataSet(Color.GREEN, "$chartTitle X")
        val lineDataSet2 = createDataSet(Color.RED, "Y")
        val lineDataSet3 = createDataSet(Color.BLUE, "Z")
        val lineDataSet4= createDataSet(Color.MAGENTA, "A1")
        val lineDataSet5 = createDataSet(Color.CYAN, "A2")
        return LineData(lineDataSet1, lineDataSet2, lineDataSet3, lineDataSet4, lineDataSet5)
    }
}