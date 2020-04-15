package com.fluffycat.sensorsmanager.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.sensors.GyroscopeSensorController
import com.fluffycat.sensorsmanager.sensors.ISensorController

class GyroscopeFragment : Fragment() {

    companion object {
        const val TAG = "GyroscopeFragmentTAG"
    }

    private lateinit var gyroscopeSensorController: ISensorController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setActivityTitle()
        return inflater.inflate(R.layout.gyroscope_fragment, container, false)
    }

    private fun setActivityTitle() {
        activity?.title = getString(R.string.gyroscope)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gyroscopeSensorController = GyroscopeSensorController(activity as Activity)
        gyroscopeSensorController.startReceivingData()
    }

    override fun onDestroy() {
        super.onDestroy()
        gyroscopeSensorController.stopReceivingData()
    }
}