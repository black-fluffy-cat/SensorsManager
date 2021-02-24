package com.fluffycat.sensorsmanager.fragments

import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
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
import com.fluffycat.sensorsmanager.values.ValuesConverter
import com.fluffycat.sensorsmanager.notification.NotificationManagerBuilder
import com.fluffycat.sensorsmanager.preferences.PreferencesManager
import com.fluffycat.sensorsmanager.services.CollectingDataService
import com.fluffycat.sensorsmanager.utils.LogFlurryEvent
import com.fluffycat.sensorsmanager.utils.getLicencesInfoString
import com.fluffycat.sensorsmanager.utils.showToast
import com.fluffycat.sensorsmanager.utils.tag
import com.fluffycat.sensorsmanager.values.UnitsProvider
import kotlinx.android.synthetic.main.settings_fragment.*
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject

class SettingsFragment : Fragment() {

    private val notificationManagerBuilder: NotificationManagerBuilder by inject()
    private val preferencesManager: PreferencesManager by inject()
    private val valuesConverter: ValuesConverter by inject()
    private val unitsProvider: UnitsProvider by inject()

    private val mainViewModel: MainViewModel by activityViewModels()

    private var flag = true

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
            bindToCollectingDataService()
            serviceValuesLabel?.isVisible = true
            notifyNotificationLabel?.isVisible = true
            notifyNotificationLabel?.setOnClickListener {
                context?.let { cxt ->
                    if (flag)
                        notificationManagerBuilder.notifyMainNotification(cxt)
                    else
                        notificationManagerBuilder.notifyServiceNotification(cxt)
                    flag = !flag
                }
            }

            startServiceLabel?.isVisible = true
            startServiceLabel?.setOnClickListener {
                Log.d(tag, "mService is: $mService")
                activity?.apply {
                    if (flag) {
                        CollectingDataService.start(applicationContext)
                        bindToCollectingDataService()
                    } else {
                        unbindService(connection)
                        CollectingDataService.stop(applicationContext)
                    }
                    flag = !flag
                }
            }
        }
    }

    private fun setOnClickListeners() {
        licencesLabel.setOnClickListener {
            LogFlurryEvent("Clicked licences info")
            activity?.let { showToast(it, getLicencesInfoString()) }
        }

        chooseDistanceUnitLabel.setOnClickListener { createChooseDistanceUnitDialog() }
    }

    private fun bindToCollectingDataService() {
        Log.d(tag, "Calling bindService, ${
            requireActivity().bindService(CollectingDataService.getServiceIntent(requireActivity().applicationContext),
                    connection, Context.BIND_AUTO_CREATE)
        }")
    }

    override fun onDestroyView() {
        unbindFromCollectingDataService()
        super.onDestroyView()
    }

    private fun unbindFromCollectingDataService() {
        try {
            activity?.apply { unbindService(connection) }
        } catch (e: IllegalArgumentException) {
            Log.e(tag, "Exception while unbinding from service")
        }
    }

    private fun createChooseDistanceUnitDialog() {
        val availableUnits = unitsProvider.getAvailableDistanceUnits()
        val availableUnitsStrings = availableUnits.map { it.toString() }.toTypedArray()
        with(AlertDialog.Builder(activity)) {
            setTitle("Select sensor")
            setSingleChoiceItems(availableUnitsStrings, -1) { dialog, item ->
                mainViewModel.saveChosenDistanceUnit(availableUnits[item])
                dialog.dismiss()
            }
            val dialog = create()
            dialog.show()
        }
    }

    private fun onServiceDataChanged(values: Triple<Float, Float, Float>) {
        if (valuesList.size < NEEDED_SAMPLES) {
            valuesList.add(values)
        } else {
            valuesList.removeAt(0)
            valuesList.add(values)
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

    private var mService: CollectingDataService? = null
    private var mBound: Boolean = false
    private val NEEDED_SAMPLES = 5
    private var valuesList = mutableListOf<Triple<Float, Float, Float>>()

    /** Defines callbacks for service binding, passed to bindService()  */
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(tag, "onServiceConnected, tag is: $tag")
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as CollectingDataService.LocalBinder
            mService = binder.service
            mBound = true
            lifecycleScope.launchWhenResumed {
                mService?.observeEventValues()?.collect { it?.let { onServiceDataChanged(it) } }
            }
        }

        override fun onBindingDied(name: ComponentName?) {
            Log.d(tag, "onBindingDied")
            super.onBindingDied(name)
        }

        override fun onNullBinding(name: ComponentName?) {
            Log.d(tag, "onNullBinding")
            super.onNullBinding(name)
        }

        override fun onServiceDisconnected(arg0: ComponentName?) {
            Log.d(tag, "onServiceDisconnected")
            mBound = false
        }
    }
}