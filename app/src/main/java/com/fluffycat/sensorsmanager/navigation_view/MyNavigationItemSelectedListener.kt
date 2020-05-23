package com.fluffycat.sensorsmanager.navigation_view

import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.fragments.*
import com.google.android.material.navigation.NavigationView

class MyNavigationItemSelectedListener(private val switchFragment: (fragment: Fragment, tag: String) -> Unit) :
    NavigationView.OnNavigationItemSelectedListener {

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.accelerometerMenuItem -> switchFragment(AccelerometerFragment(), AccelerometerFragment.TAG)
            R.id.gyroscopeMenuItem -> switchFragment(GyroscopeFragment(), GyroscopeFragment.TAG)
            R.id.magneticFieldSensorMenuItem -> switchFragment(MagneticFieldFragment(), MagneticFieldFragment.TAG)
            R.id.lightSensorMenuItem -> switchFragment(LightFragment(), LightFragment.TAG)
            R.id.heartbeatSensorMenuItem -> switchFragment(HeartbeatFragment(), HeartbeatFragment.TAG)
            R.id.settingsMenuItem -> switchFragment(SettingsFragment(), SettingsFragment.TAG)
//            R.id.modulesStatusMenuItem -> switchFragment(ModulesStatusFragment(), ModulesStatusFragment.TAG)
//            R.id.otherMenuItem -> switchFragment(OtherFragment(), OtherFragment.TAG)
//            R.id.customMenuItem -> switchFragment(CustomFragment(), CustomFragment.TAG)
//            else -> switchFragment(ExampleFragment(menuTitle), ExampleFragment.TAG)
        }
        return true
    }
}