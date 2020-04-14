package com.fluffycat.sensorsmanager.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fluffycat.sensorsmanager.R

class GyroscopeFragment: Fragment() {

    companion object {
        const val TAG = "GyroscopeFragmentTAG"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setActivityTitle()
        return inflater.inflate(R.layout.gyroscope_fragment, container, false)
    }

    private fun setActivityTitle() {
        activity?.title = getString(R.string.gyroscope)
    }
}