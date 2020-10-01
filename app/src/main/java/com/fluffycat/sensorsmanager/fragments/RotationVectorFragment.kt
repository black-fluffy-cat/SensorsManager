package com.fluffycat.sensorsmanager.fragments

import android.graphics.Color
import com.github.mikephil.charting.data.LineData

class RotationVectorFragment : BaseChartFragment() {

    override fun createLineData(): LineData {
        val lineDataSet1 = createDataSet(Color.GREEN, "X")
        val lineDataSet2 = createDataSet(Color.RED, "Y")
        val lineDataSet3 = createDataSet(Color.BLUE, "Z")
        val lineDataSet4 = createDataSet(Color.MAGENTA, "A1")
        val lineDataSet5 = createDataSet(Color.CYAN, "A2")
        return LineData(lineDataSet1, lineDataSet2, lineDataSet3, lineDataSet4, lineDataSet5)
    }
}