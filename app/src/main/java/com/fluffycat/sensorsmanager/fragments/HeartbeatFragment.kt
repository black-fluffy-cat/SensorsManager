package com.fluffycat.sensorsmanager.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.lifecycle.Observer
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.utils.HEART_RATE_REQUEST_CODE
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import kotlinx.android.synthetic.main.heartbeat_fragment.*

class HeartbeatFragment : BaseChartFragment() {

    override val layoutResource: Int = R.layout.heartbeat_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if ((checkSelfPermission(activity!!, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            requestPermissions(arrayOf(Manifest.permission.BODY_SENSORS), HEART_RATE_REQUEST_CODE)
        }

        sensorController?.additionalData?.observe(this, Observer { additionalCode ->
            onAdditionalDataChanged(additionalCode)
        })

        super.onViewCreated(view, savedInstanceState)
    }

    override fun setupView() {
        arguments?.getInt(SENSOR_TYPE_ARG_NAME, 0)?.let { sensorType ->
            val fragmentTitle = getFragmentTitle(sensorType)
            setActivityTitle(fragmentTitle)
        }
        heartbeatChart.data = lineData
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == HEART_RATE_REQUEST_CODE && grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            sensorController?.startReceivingData()
        }
    }

    override fun onDataChanged(event: SensorEvent) {
        heartbeatFragmentText.text = event.values[0].toString()
        lineData.apply {
            addEntry(Entry(getDataSetByIndex(0).entryCount.toFloat(), event.values[0]), 0)

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