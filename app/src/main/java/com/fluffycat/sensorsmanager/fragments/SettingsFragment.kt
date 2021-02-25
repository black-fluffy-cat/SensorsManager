package com.fluffycat.sensorsmanager.fragments

import android.app.AlertDialog
import android.hardware.SensorEvent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.fluffycat.sensorsmanager.BuildConfig
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.activities.MainViewModel
import com.fluffycat.sensorsmanager.sensors.SensorControllerProvider
import com.fluffycat.sensorsmanager.sensors.SensorType
import com.fluffycat.sensorsmanager.services.CollectingDataService
import com.fluffycat.sensorsmanager.utils.LogFlurryEvent
import com.fluffycat.sensorsmanager.utils.getLicencesInfoString
import com.fluffycat.sensorsmanager.utils.showToast
import com.fluffycat.sensorsmanager.values.UnitsProvider
import com.fluffycat.sensorsmanager.values.ValuesConverter
import kotlinx.android.synthetic.main.settings_fragment.*
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject

class SettingsFragment : Fragment() {

    companion object {
        private const val NEEDED_SAMPLES = 5
    }

    private var valuesList = mutableListOf<Triple<Float, Float, Float>>()

    private val valuesConverter: ValuesConverter by inject()
    private val unitsProvider: UnitsProvider by inject()
    private val sensorControllerProvider: SensorControllerProvider by inject()

    private val mainViewModel: MainViewModel by activityViewModels()
    private var flag: Boolean = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setActivityTitle()
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    private fun setActivityTitle() {
        activity?.title = getString(R.string.settings)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()

        if (BuildConfig.DEBUG) {
            setupDebugOptions()
        }
    }

    private fun setOnClickListeners() {
        licencesLabel.setOnClickListener {
            LogFlurryEvent("Clicked licences info")
            activity?.let { showToast(it, getLicencesInfoString()) }
        }

        chooseDistanceUnitLabel.setOnClickListener { createChooseDistanceUnitDialog() }
    }

    private fun setupDebugOptions() {
        serviceValuesLabel?.isVisible = true
        startServiceLabel?.isVisible = true
        startServiceLabel?.setOnClickListener {
            activity?.apply {
                lifecycleScope.launchWhenResumed {
                    sensorControllerProvider.getSensorController(SensorType.Accelerometer).observeSensorCurrentData()
                        .collect { it?.let { onServiceDataChanged(it) } }
                }
                if (flag) {
                    CollectingDataService.startCollectingData(applicationContext)
                } else {
                    CollectingDataService.stop(applicationContext)
                }
                flag = !flag
            }
        }
    }

    private fun createChooseDistanceUnitDialog() {
        val availableUnits = unitsProvider.getAvailableDistanceUnits()
        val availableUnitsStrings = availableUnits.map { it.toString() }.toTypedArray()
        with(AlertDialog.Builder(activity)) {
            setTitle("Select unit")
            setSingleChoiceItems(availableUnitsStrings, -1) { dialog, item ->
                mainViewModel.saveChosenDistanceUnit(availableUnits[item])
                dialog.dismiss()
            }
            val dialog = create()
            dialog.show()
        }
    }

    private fun onServiceDataChanged(event: SensorEvent) {
        val valuesTriple = Triple(event.values[0], event.values[1], event.values[2])
        if (valuesList.size < NEEDED_SAMPLES) {
            valuesList.add(valuesTriple)
        } else {
            valuesList.removeAt(0)
            valuesList.add(valuesTriple)
            var averageX = 0f
            var averageY = 0f
            var averageZ = 0f

            valuesList.forEach {
                averageX += it.first
                averageY += it.second
                averageZ += it.third
            }
            averageX = valuesConverter.roundValue(averageX / 5, 8)
            averageY = valuesConverter.roundValue(averageY / 5, 8)
            averageZ = valuesConverter.roundValue(averageZ / 5, 8)

            serviceValuesLabel?.text = "$averageX $averageY $averageZ"
        }
    }
}