package com.fluffycat.sensorsmanager.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.lifecycle.lifecycleScope
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.databinding.HeartbeatFragmentBinding
import com.fluffycat.sensorsmanager.utils.HEART_RATE_REQUEST_CODE
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry

class HeartbeatFragment : BaseChartFragment() {

    override val layoutResource: Int = R.layout.heartbeat_fragment

    private var heartbeatBinding: HeartbeatFragmentBinding? = null

    override fun isPermissionGranted(context: Context) = isBodySensorsPermissionGranted(context)
    override fun requestNeededPermission() = requestBodySensorsPermission()

    private fun isBodySensorsPermissionGranted(context: Context) =
        (checkSelfPermission(context, Manifest.permission.BODY_SENSORS) == PackageManager.PERMISSION_GRANTED)
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH

    private fun requestBodySensorsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            requestPermissions(arrayOf(Manifest.permission.BODY_SENSORS), HEART_RATE_REQUEST_CODE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        context?.let { cxt ->
            if (!isBodySensorsPermissionGranted(cxt)) {
                requestBodySensorsPermission()
            }
        }

        lifecycleScope.launchWhenResumed {
            sensorController?.observeAdditionalData()?.collect { if (it != null) onAdditionalDataChanged(it) }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        heartbeatBinding = null
        super.onDestroyView()
    }

    override fun onSensorError() {
        heartbeatBinding?.heartbeatChart?.description = Description().apply { text = "Sensor error occurred" }
    }

    override fun setupView() {
        val binding = HeartbeatFragmentBinding.bind(requireView()).also { heartbeatBinding = it }
        setActivityTitle()
        binding.heartbeatChart.data = lineData
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == HEART_RATE_REQUEST_CODE && grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            sensorController?.startReceivingData()
        }
    }

    override fun onDataChanged(event: SensorEvent) {
        val binding = heartbeatBinding ?: return
        binding.heartbeatFragmentText.text = event.values[0].toString()
        lineData.apply {
            addEntry(Entry(getDataSetByIndex(0).entryCount.toFloat(), event.values[0]), 0)

            notifyDataChanged()
            binding.heartbeatChart.notifyDataSetChanged()
            binding.heartbeatChart.setVisibleXRangeMaximum(100F)
            binding.heartbeatChart.moveViewTo(entryCount.toFloat(), 0F, YAxis.AxisDependency.RIGHT)
        }
    }

    private fun onAdditionalDataChanged(additionalCode: Int) {
        Log.d(tag, "additionalCode: $additionalCode")
        val binding = heartbeatBinding ?: return
        when (additionalCode) {
            SensorManager.SENSOR_STATUS_UNRELIABLE -> {
                binding.heartbeatFragmentAdditionalText.text = "Please put your finger on the sensor and wait for measurement"
            }
            else -> {
                binding.heartbeatFragmentAdditionalText.text = ""
            }
        }
    }
}
