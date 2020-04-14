package com.fluffycat.sensorsmanager.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.sensors.AccelerometerSensorController
import com.fluffycat.sensorsmanager.sensors.ISensorController

class AccelerometerFragment : Fragment() {

    companion object {
        const val TAG = "AccelerometerFragmentTAG"
    }

    private lateinit var accelerometerSensorController: ISensorController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setActivityTitle()
        return inflater.inflate(R.layout.accelerometer_fragment, container, false)
    }

    private fun setActivityTitle() {
        activity?.title = getString(R.string.accelerometer)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accelerometerSensorController = AccelerometerSensorController(activity as Activity)
        accelerometerSensorController.startReceivingData()
    }

    override fun onDestroy() {
        super.onDestroy()
        accelerometerSensorController.stopReceivingData()
    }
}