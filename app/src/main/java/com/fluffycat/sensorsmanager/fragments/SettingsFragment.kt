package com.fluffycat.sensorsmanager.fragments

import android.app.AlertDialog
import android.hardware.SensorEvent
import android.os.Bundle
import android.view.View
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
import com.fluffycat.sensorsmanager.utils.getLicensesInfoString
import com.fluffycat.sensorsmanager.utils.showToast
import com.fluffycat.sensorsmanager.values.UnitsProvider
import com.fluffycat.sensorsmanager.values.ValuesConverter
import kotlinx.android.synthetic.main.settings_fragment.*
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import java.util.concurrent.atomic.AtomicBoolean

class SettingsFragment : Fragment() {

    companion object {
        private const val NEEDED_SAMPLES = 5
    }

    private val valuesList = mutableListOf<SensorEvent>()

    private val valuesConverter: ValuesConverter by inject()
    private val unitsProvider: UnitsProvider by inject()
    private val sensorControllerProvider: SensorControllerProvider by inject()

    private val mainViewModel: MainViewModel by activityViewModels()
    private val flag = AtomicBoolean(true)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActivityTitle()
        setOnClickListeners()

        if (BuildConfig.DEBUG) setupDebugOptions()
    }

    private fun setActivityTitle() {
        activity?.title = getString(R.string.settings)
    }

    private fun setOnClickListeners() {
        licensesLabel.setOnClickListener {
            LogFlurryEvent("Clicked licenses info")
            activity?.let { showToast(it, getLicensesInfoString()) }
        }
        chooseDistanceUnitLabel.setOnClickListener { createChooseDistanceUnitDialog() }
    }

    private fun setupDebugOptions() {
        serviceValuesLabel?.isVisible = true
        startServiceLabel?.isVisible = true
        startServiceLabel?.setOnClickListener {
            observeSensorData()
            if (flag.get()) context?.let { CollectingDataService.startCollectingData(it) }
            else context?.let { CollectingDataService.stop(it) }
            flag.set(!flag.get())
        }
    }

    private fun observeSensorData() {
        lifecycleScope.launchWhenResumed {
            sensorControllerProvider.getSensorController(SensorType.Accelerometer).observeSensorCurrentData()
                .collect { it?.let { onSensorDataChanged(it) } }
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

    private fun onSensorDataChanged(event: SensorEvent) {
        if (valuesList.size < NEEDED_SAMPLES) {
            valuesList.add(event)
        } else {
            valuesList.removeAt(0)
            valuesList.add(event)
            var averageX = 0f
            var averageY = 0f
            var averageZ = 0f

            valuesList.forEach {
                averageX += it.values[0]
                averageY += it.values[1]
                averageZ += it.values[2]
            }

            averageX = valuesConverter.roundValue(averageX / 5, 8)
            averageY = valuesConverter.roundValue(averageY / 5, 8)
            averageZ = valuesConverter.roundValue(averageZ / 5, 8)

            val labelText = "$averageX $averageY $averageZ"
            serviceValuesLabel?.text = labelText
        }
    }
}