package com.fluffycat.sensorsmanager.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.utils.getLicencesInfoString
import com.fluffycat.sensorsmanager.utils.showToast
import kotlinx.android.synthetic.main.settings_fragment.*

class SettingsFragment : Fragment() {

    companion object {
        const val TAG = "SettingsFragmentTAG"
    }

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
                showToast(activity, getLicencesInfoString())
            }
        }
    }
}