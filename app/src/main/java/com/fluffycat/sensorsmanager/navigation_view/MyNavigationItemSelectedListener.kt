package com.fluffycat.sensorsmanager.navigation_view

import android.util.Log
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.fragments.*
import com.fluffycat.sensorsmanager.utils.LogFlurryEvent
import com.google.android.gms.ads.InterstitialAd
import com.google.android.material.navigation.NavigationView

private const val CLICKS_BEFORE_AD = 3

class MyNavigationItemSelectedListener(
        private val switchFragment: (fragment: Fragment) -> Unit,
        private val mInterstitialAd: InterstitialAd) :
    NavigationView.OnNavigationItemSelectedListener {

    private var menuClicks = 0

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.accelerometerMenuItem -> switchFragment(AccelerometerFragment())
            R.id.gyroscopeMenuItem -> switchFragment(GyroscopeFragment())
            R.id.magneticFieldSensorMenuItem -> switchFragment(MagneticFieldFragment())
            R.id.lightSensorMenuItem -> switchFragment(LightFragment())
            R.id.heartbeatSensorMenuItem -> switchFragment(HeartbeatFragment())
//            R.id.microphoneMenuItem -> switchFragment(MicrophoneFragment)
            R.id.linearAccelerationSensorMenuItem -> switchFragment(LinearAccelerationFragment())
            R.id.proximitySensorMenuItem -> switchFragment(ProximityFragment())
            R.id.rotationVectorMenuItem -> switchFragment(RotationVectorFragment())
            R.id.settingsMenuItem -> switchFragment(SettingsFragment())
//            R.id.modulesStatusMenuItem -> switchFragment(ModulesStatusFragment)
//            R.id.otherMenuItem -> switchFragment(OtherFragment)
//            R.id.customMenuItem -> switchFragment(CustomFragment)
//            else -> switchFragment(ExampleFragment(menuTitle)
        }

        if (++menuClicks == CLICKS_BEFORE_AD) {
            menuClicks = 0
            if (mInterstitialAd.isLoaded) {
                LogFlurryEvent("Showing mInterstitialAd")
                mInterstitialAd.show()
            } else {
                LogFlurryEvent("mInterstitialAd not loaded yet")
                Log.d("ABAB", "The interstitial wasn't loaded yet.")
            }
        }
        return true
    }
}