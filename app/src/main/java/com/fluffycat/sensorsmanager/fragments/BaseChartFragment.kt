package com.fluffycat.sensorsmanager.fragments

import android.content.Context
import android.graphics.Color
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.converter.ValuesConverter
import com.fluffycat.sensorsmanager.sensors.*
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.chart_fragment.*

const val SENSOR_TYPE_ARG_NAME = "sensorType"

open class BaseChartFragment : Fragment() {

    private val valuesConverter = ValuesConverter()

    protected var sensorController: ISensorController? = null
    protected open val layoutResource = R.layout.chart_fragment
    protected lateinit var lineData: LineData

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val sensorManager = context?.getSystemService(Context.SENSOR_SERVICE) as SensorManager?
        val sensorType = arguments?.getInt(SENSOR_TYPE_ARG_NAME, 0)
        if (sensorType != null && sensorManager != null) {
            sensorController = SensorController(sensorManager, sensorType)
            lineData = createLineData()
        } else {
            onSensorError()
        }
        Log.d("ABAB", "context: $context, smanager: $sensorManager")
        return inflater.inflate(layoutResource, container, false)
    }

    private fun getFragmentTitle(sensorType: Int): String = when (sensorType) {
        ACCELEROMETER_SENSOR_TYPE -> getString(R.string.accelerometer)
        GYROSCOPE_SENSOR_TYPE -> getString(R.string.gyroscope)
        LIGHT_SENSOR_TYPE -> getString(R.string.light)
        LINEAR_ACCELERATION_SENSOR_TYPE -> getString(R.string.linearAcceleration)
        MAGNETIC_FIELD_SENSOR_TYPE -> getString(R.string.magnetic_field)
        PROXIMITY_SENSOR_TYPE -> getString(R.string.proximity)
        HEART_RATE_SENSOR_TYPE -> getString(R.string.heartbeat)
        else -> ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActivityTitle()
        setupView()

        // TODO Can I observe it here and forget about it?
        sensorController?.sensorCurrentData?.observe(this, Observer { sensorEvent ->
            onDataChanged(sensorEvent)
        })
    }

    protected open fun setupView() {
        mainChart.data = lineData
        mainChart.description = Description().apply { text = "" }
        mainChartSensorInfoLabel.text = sensorController?.getSensorInfo()
    }

    private fun onSensorError() {
        mainChart.description = Description().apply { text = "Sensor error occurred" }
        mainChartSensorInfoLabel.text = getString(R.string.error)
    }

    override fun onStart() {
        super.onStart()
        val registeringSuccessful = sensorController?.startReceivingData()
        if (registeringSuccessful != true) {
            onSensorError()
        }
    }

    override fun onStop() {
        super.onStop()
        sensorController?.stopReceivingData()
    }

    protected open fun createLineData(): LineData {
        val lineDataSet1 = createDataSet(Color.GREEN, "X")
        val lineDataSet2 = createDataSet(Color.RED, "Y")
        val lineDataSet3 = createDataSet(Color.BLUE, "Z")
        return LineData(lineDataSet1, lineDataSet2, lineDataSet3)
    }

    protected fun setActivityTitle() {
        arguments?.getInt(SENSOR_TYPE_ARG_NAME, 0)?.let { sensorType ->
            val fragmentTitle = getFragmentTitle(sensorType)
            activity?.title = fragmentTitle
        }
    }

    protected open fun createDataSet(dataSetColor: Int, label: String) = LineDataSet(ArrayList(), label).apply {
        setDrawCircles(false)
        lineWidth = 3.1f
        color = dataSetColor
    }

    open fun onDataChanged(event: SensorEvent) {
        val convertedValues = event.values.copyOf().apply {
            forEachIndexed { index, value ->
                this[index] = valuesConverter.convertValueToChosenUnit(value, event.sensor)
            }
        }

        val roundedValues = convertedValues.copyOf().apply {
            forEachIndexed { index, value -> this[index] = valuesConverter.roundValue(value) }
        }

        val labelTexts = mutableListOf<String>().apply {
            roundedValues.forEach { this.add(valuesConverter.convertValueToStringWithSymbol(it, event.sensor)) }
        }

        setupLabelsTexts(labelTexts)

        lineData.apply {
            convertedValues.sliceArray(IntRange(0, event.values.size - 1)).forEachIndexed { index, value ->
                addEntry(Entry(getDataSetByIndex(index).entryCount.toFloat(), value), index)
            }

            notifyDataChanged()
            mainChart.notifyDataSetChanged()
            mainChart.setVisibleXRangeMaximum(500F)
            mainChart.moveViewTo(entryCount.toFloat(), 0F, YAxis.AxisDependency.RIGHT)
        }
    }

    private fun setupLabelsTexts(labelTexts: MutableList<String>) {
        labelTexts.getOrNull(0)?.let { text ->
            mainChartXValueInfoLabel?.text = getString(R.string.xChartLabel, text)
        }

        labelTexts.getOrNull(1)?.let { text ->
            mainChartYValueInfoLabel?.text = getString(R.string.yChartLabel, text)
        }

        labelTexts.getOrNull(2)?.let { text ->
            mainChartZValueInfoLabel?.text = getString(R.string.zChartLabel, text)
        }
    }
}