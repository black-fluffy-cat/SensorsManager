package com.fluffycat.sensorsmanager.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.SensorsManagerApplication
import com.fluffycat.sensorsmanager.preferences.DISTANCE_FEET_VALUE
import com.fluffycat.sensorsmanager.preferences.DISTANCE_METERS_VALUE
import com.fluffycat.sensorsmanager.preferences.PreferencesManager
import com.fluffycat.sensorsmanager.utils.LogFlurryEvent
import com.fluffycat.sensorsmanager.utils.getLicencesInfoString
import com.fluffycat.sensorsmanager.utils.showToast
import kotlinx.android.synthetic.main.settings_fragment.*

class SettingsFragment : Fragment() {

    companion object {
        const val TAG = "SettingsFragmentTAG"
    }

    private val valuesArray = arrayOf("METERS", "FEET")
    private val preferencesManager: PreferencesManager = SensorsManagerApplication.instance.preferencesManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setActivityTitle()
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    private fun setActivityTitle() {
        activity?.title = getString(R.string.settings)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        licencesLabel.setOnClickListener {
            activity?.let { activity ->
                LogFlurryEvent("Clicked licences info")
                showToast(activity, getLicencesInfoString())
            }
        }

        chooseDistanceUnitLabel.setOnClickListener {
            createAlertDialog()
        }
    }

    private fun createAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder.setTitle("Select sensor")
        alertDialogBuilder.setSingleChoiceItems(valuesArray, -1) { dialog, item ->
            when (valuesArray[item]) {
                "METERS" -> preferencesManager.saveChosenDistanceUnit(DISTANCE_METERS_VALUE)
                "FEET" -> preferencesManager.saveChosenDistanceUnit(DISTANCE_FEET_VALUE)
            }
            dialog.dismiss()
        }
        val alert = alertDialogBuilder.create()
        alert.show()
    }
}