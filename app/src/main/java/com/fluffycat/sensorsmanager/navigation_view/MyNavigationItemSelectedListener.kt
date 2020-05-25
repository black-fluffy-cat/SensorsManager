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
        private val switchFragment: (fragment: Fragment, tag: String) -> Unit,
        private val mInterstitialAd: InterstitialAd) :
    NavigationView.OnNavigationItemSelectedListener {

    private var menuClicks = 0

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.accelerometerMenuItem -> switchFragment(AccelerometerFragment(), AccelerometerFragment.TAG)
            R.id.gyroscopeMenuItem -> switchFragment(GyroscopeFragment(), GyroscopeFragment.TAG)
            R.id.magneticFieldSensorMenuItem -> switchFragment(MagneticFieldFragment(), MagneticFieldFragment.TAG)
            R.id.lightSensorMenuItem -> switchFragment(LightFragment(), LightFragment.TAG)
            R.id.heartbeatSensorMenuItem -> switchFragment(HeartbeatFragment(), HeartbeatFragment.TAG)
//            R.id.microphoneMenuItem -> switchFragment(MicrophoneFragment(), MicrophoneFragment.TAG)
            R.id.linearAccelerationSensorMenuItem -> switchFragment(LinearAccelerationFragment(),
                    LinearAccelerationFragment.TAG)
            R.id.proximitySensorMenuItem -> switchFragment(ProximityFragment(), ProximityFragment.TAG)
            R.id.rotationVectorMenuItem -> switchFragment(RotationVectorFragment(), RotationVectorFragment.TAG)
            R.id.settingsMenuItem -> switchFragment(SettingsFragment(), SettingsFragment.TAG)
//            R.id.modulesStatusMenuItem -> switchFragment(ModulesStatusFragment(), ModulesStatusFragment.TAG)
//            R.id.otherMenuItem -> switchFragment(OtherFragment(), OtherFragment.TAG)
//            R.id.customMenuItem -> switchFragment(CustomFragment(), CustomFragment.TAG)
//            else -> switchFragment(ExampleFragment(menuTitle), ExampleFragment.TAG)
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