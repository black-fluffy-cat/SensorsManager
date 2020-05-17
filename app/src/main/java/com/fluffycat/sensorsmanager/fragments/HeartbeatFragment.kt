package com.fluffycat.sensorsmanager.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.SensorsManagerApplication
import com.fluffycat.sensorsmanager.sensors.HeartbeatSensorController
import com.fluffycat.sensorsmanager.utils.HEART_RATE_REQUEST_CODE
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.heartbeat_fragment.*
import java.util.*

class HeartbeatFragment : Fragment() {

    companion object {
        const val TAG = "HeartbeatFragmentTAG"
        const val chartTitle = "Heartbeat rate"
    }

    // TODO Change to interface type :(
    private lateinit var heartbeatSensorController: HeartbeatSensorController
    private var lineData: LineData

    init {
        val lineDataSet1: LineDataSet = createDataSet(Color.RED, chartTitle)
        lineData = LineData(lineDataSet1)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setActivityTitle()
        return inflater.inflate(R.layout.heartbeat_fragment, container, false)
    }

    private fun setActivityTitle() {
        activity?.title = getString(R.string.heartbeat)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if ((checkSelfPermission(activity!!,
                    Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            requestPermissions(arrayOf(Manifest.permission.BODY_SENSORS),
                    HEART_RATE_REQUEST_CODE)
        }

        heartbeatSensorController = HeartbeatSensorController(context ?: SensorsManagerApplication.getContext())

        // TODO Can I observe it here and forget about it?
        heartbeatSensorController.sensorCurrentData.observe(this, Observer { sensorEvent ->
            onDataChanged(sensorEvent)
        })

        heartbeatSensorController.additionalData.observe(this, Observer { additionalCode ->
            onAdditionalDataChanged(additionalCode)
        })

        heartbeatChart.data = lineData
    }

    @Suppress("SameParameterValue")
    private fun createDataSet(dataSetColor: Int, label: String) = LineDataSet(ArrayList(), label).apply {
        setDrawCircles(false)
        lineWidth = 5f
        color = dataSetColor
    }

    override fun onStart() {
        super.onStart()
        heartbeatSensorController.startReceivingData()
    }

    override fun onStop() {
        super.onStop()
        heartbeatSensorController.stopReceivingData()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == HEART_RATE_REQUEST_CODE && grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            heartbeatSensorController.startReceivingData()
        }
    }

    private fun onDataChanged(sensorEvent: SensorEvent) {
        heartbeatFragmentText.text = sensorEvent.values[0].toString()
        lineData.apply {
            addEntry(Entry(getDataSetByIndex(0).entryCount.toFloat(), sensorEvent.values[0]), 0)

            notifyDataChanged()
            heartbeatChart.notifyDataSetChanged()
            heartbeatChart.setVisibleXRangeMaximum(100F)
            heartbeatChart.moveViewTo(entryCount.toFloat(), 0F, YAxis.AxisDependency.RIGHT)
        }
    }

    private fun onAdditionalDataChanged(additionalCode: Int) {
        Log.d(tag, "additionalCode: $additionalCode")
        when (additionalCode) {
            SensorManager.SENSOR_STATUS_UNRELIABLE -> {
                heartbeatFragmentAdditionalText.text = "Please put your finger on the sensor and wait for measurement"
            }
            else -> {
                heartbeatFragmentAdditionalText.text = ""
            }
        }
    }
}