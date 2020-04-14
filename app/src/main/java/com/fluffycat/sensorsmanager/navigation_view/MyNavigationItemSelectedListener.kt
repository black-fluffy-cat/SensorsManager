package com.fluffycat.sensorsmanager.navigation_view

import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.fragments.*
import com.google.android.material.navigation.NavigationView

class MyNavigationItemSelectedListener(private val activity: AppCompatActivity,
                                       private val switchFragment: (fragment: Fragment, tag: String) -> Unit) :
    NavigationView.OnNavigationItemSelectedListener {

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        val menuTitle = menuItem.title.toString()
        when (menuItem.itemId) {
            R.id.accelerometerMenuItem -> switchFragment(AccelerometerFragment(), AccelerometerFragment.TAG)
            R.id.customMenuItem -> switchFragment(CustomFragment(), CustomFragment.TAG)
            R.id.gyroscopeMenuItem -> switchFragment(GyroscopeFragment(), GyroscopeFragment.TAG)
            R.id.magneticFieldSensorMenuItem -> switchFragment(MagneticFieldFragment(), MagneticFieldFragment.TAG)
            R.id.modulesStatusMenuItem -> switchFragment(ModulesStatusFragment(), ModulesStatusFragment.TAG)
            R.id.otherMenuItem -> switchFragment(OtherFragment(), OtherFragment.TAG)
            R.id.settingsMenuItem -> switchFragment(SettingsFragment(), SettingsFragment.TAG)
            else -> switchFragment(ExampleFragment(menuTitle), ExampleFragment.TAG)
        }
        return true
    }
}