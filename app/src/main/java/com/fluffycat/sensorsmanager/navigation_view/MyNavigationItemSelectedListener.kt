package com.fluffycat.sensorsmanager.navigation_view

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.activities.MainActivity
import com.fluffycat.sensorsmanager.fragments.*
import com.fluffycat.sensorsmanager.sensors.*
import com.google.android.material.navigation.NavigationView


class MyNavigationItemSelectedListener(private val mainActivity: MainActivity) :
    NavigationView.OnNavigationItemSelectedListener {


    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        val sensorType = createSensorType(menuItem.itemId)
        val fragment = createProperFragment(menuItem.itemId)

        val args = Bundle()
        args.putInt(SENSOR_TYPE_ARG_NAME, sensorType)
        fragment.arguments = args
        mainActivity.onDrawerItemSelected(fragment)

        return true
    }

    private fun createSensorType(itemId: Int): Int = when (itemId) {
        R.id.accelerometerMenuItem -> ACCELEROMETER_SENSOR_TYPE
        R.id.gyroscopeMenuItem -> GYROSCOPE_SENSOR_TYPE
        R.id.magneticFieldSensorMenuItem -> MAGNETIC_FIELD_SENSOR_TYPE
        R.id.lightSensorMenuItem -> LIGHT_SENSOR_TYPE
        R.id.heartbeatSensorMenuItem -> HEART_RATE_SENSOR_TYPE
        R.id.linearAccelerationSensorMenuItem -> LINEAR_ACCELERATION_SENSOR_TYPE
        R.id.proximitySensorMenuItem -> PROXIMITY_SENSOR_TYPE
        R.id.rotationVectorMenuItem -> ROTATION_VECTOR_SENSOR_TYPE
        else -> 0
    }

    private fun createProperFragment(itemId: Int): Fragment = when (itemId) {
        R.id.accelerometerMenuItem, R.id.gyroscopeMenuItem, R.id.magneticFieldSensorMenuItem,
        R.id.lightSensorMenuItem, R.id.linearAccelerationSensorMenuItem,
        R.id.proximitySensorMenuItem -> BaseChartFragment()
        R.id.rotationVectorMenuItem -> RotationVectorFragment()
        R.id.heartbeatSensorMenuItem -> HeartbeatFragment()
        R.id.settingsMenuItem -> SettingsFragment()
        else -> ExampleFragment("Example")
    }
}