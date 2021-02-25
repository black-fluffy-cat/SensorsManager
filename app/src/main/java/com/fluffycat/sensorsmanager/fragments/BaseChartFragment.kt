package com.fluffycat.sensorsmanager.fragments

import android.content.Context
import android.graphics.Color
import android.hardware.SensorEvent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.sensors.ISensorController
import com.fluffycat.sensorsmanager.sensors.SensorControllerProvider
import com.fluffycat.sensorsmanager.sensors.SensorType
import com.fluffycat.sensorsmanager.values.ValuesConverter
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.chart_fragment.*
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject

const val SENSOR_TYPE_ARG_NAME = "sensorType"

open class BaseChartFragment : Fragment() {

    private val valuesConverter: ValuesConverter by inject()
    private val sensorControllerProvider: SensorControllerProvider by inject()

    protected var sensorController: ISensorController? = null
    protected open val layoutResource = R.layout.chart_fragment
    protected lateinit var lineData: LineData

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutResource, container, false)
    }

    private fun getFragmentTitle(sensorType: SensorType): String = when (sensorType) {
        SensorType.Accelerometer -> getString(R.string.accelerometer)
        SensorType.Gyroscope -> getString(R.string.gyroscope)
        SensorType.Light -> getString(R.string.light)
        SensorType.LinearAcceleration -> getString(R.string.linearAcceleration)
        SensorType.MagneticField -> getString(R.string.magnetic_field)
        SensorType.Proximity -> getString(R.string.proximity)
        SensorType.HeartRate -> getString(R.string.heartbeat)
        SensorType.RotationVector -> getString(R.string.rotationVector)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActivityTitle()

        val sensorType = arguments?.getParcelable(SENSOR_TYPE_ARG_NAME) ?: SensorType.Accelerometer
        sensorController = sensorControllerProvider.getSensorController(sensorType)
        lineData = createLineData()
        setupView()

        lifecycleScope.launchWhenResumed {
            sensorController?.observeSensorCurrentData()?.collect { event ->
                if (event != null) onDataChanged(event)
            }
        }

    }

    protected open fun setupView() {
        mainChart.data = lineData
        mainChart.description = Description().apply { text = "" }
        mainChartSensorInfoLabel.text = sensorController?.getSensorInfo()
    }

    open fun onSensorError() {
        mainChart.description = Description().apply { text = "Sensor error occurred" }
        mainChartSensorInfoLabel.text = getString(R.string.error)
    }

    open fun isPermissionGranted(context: Context) = true
    open fun requestNeededPermission() {
        /* no-op */
    }

    override fun onStart() {
        super.onStart()
        context?.apply {
            if (isPermissionGranted(this)) {
                startReceivingData()
            } else {
                requestNeededPermission()
            }
        }
    }

    private fun startReceivingData() {
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
        arguments?.getParcelable<SensorType>(SENSOR_TYPE_ARG_NAME)?.let { sensorType ->
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