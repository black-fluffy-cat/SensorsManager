package com.fluffycat.sensorsmanager.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.sensors.ISensorController
import com.fluffycat.sensorsmanager.sensors.LightSensorController

class LightFragment : Fragment() {

    companion object {
        const val TAG = "LightFragmentTAG"
    }

    private lateinit var lightSensorController: ISensorController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setActivityTitle()
        return inflater.inflate(R.layout.light_fragment, container, false)
    }

    private fun setActivityTitle() {
        activity?.title = getString(R.string.light)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lightSensorController = LightSensorController(activity as Activity)
        lightSensorController.startReceivingData()
    }

    override fun onDestroy() {
        super.onDestroy()
        lightSensorController.stopReceivingData()
    }
}