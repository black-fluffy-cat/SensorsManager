package com.fluffycat.sensorsmanager.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.SensorEvent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.SensorsManagerApplication
import com.fluffycat.sensorsmanager.sensors.HeartbeatSensorController
import com.fluffycat.sensorsmanager.sensors.ISensorController
import com.fluffycat.sensorsmanager.utils.HEART_RATE_REQUEST_CODE
import kotlinx.android.synthetic.main.heartbeat_fragment.*

class HeartbeatFragment : Fragment() {

    companion object {
        const val TAG = "HeartbeatFragmentTAG"
    }

    private lateinit var heartbeatSensorController: ISensorController

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

        heartbeatSensorController.sensorCurrentData.observe(this, Observer { sensorEvent ->
            onDataChanged(sensorEvent)
        })
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
    }
}