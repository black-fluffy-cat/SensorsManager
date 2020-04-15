package com.fluffycat.sensorsmanager.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.sensors.ISensorController
import com.fluffycat.sensorsmanager.sensors.MagneticFieldSensorController

class MagneticFieldFragment : Fragment() {

    companion object {
        const val TAG = "MagneticFieldTAG"
    }

    private lateinit var magneticFieldSensorController: ISensorController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setActivityTitle()
        return inflater.inflate(R.layout.magnetic_field_fragment, container, false)
    }

    private fun setActivityTitle() {
        activity?.title = getString(R.string.magnetic_field)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        magneticFieldSensorController = MagneticFieldSensorController(activity as Activity)
        magneticFieldSensorController.startReceivingData()
    }

    override fun onDestroy() {
        super.onDestroy()
        magneticFieldSensorController.stopReceivingData()
    }
}