package com.fluffycat.sensorsmanager.activities

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.fragments.AccelerometerFragment
import com.fluffycat.sensorsmanager.navigation_view.MyNavigationItemSelectedListener
import com.fluffycat.sensorsmanager.sensors.*
import com.fluffycat.sensorsmanager.utils.LogFlurryEvent
import com.fluffycat.sensorsmanager.utils.tag
import com.github.mikephil.charting.utils.Utils
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        const val CURRENT_FRAGMENT_SAVED_STATE = "currentFragment"
    }

    private var currentFragment: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Utils.init(this) // For first chart to have proper lines size
        setupDrawerViewListener()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupNavigationView()
        setCurrentFragment(savedInstanceState)

        initAds()
        LogFlurryEvent("MainActivity onCreate")
    }

    private fun setCurrentFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            switchFragment(AccelerometerFragment(), AccelerometerFragment.TAG)
        }
    }

    private fun switchFragment(fragment: Fragment, tag: String) {
        LogFlurryEvent("switchFragment to $tag")
        currentFragment = fragment.tag ?: ""
        supportFragmentManager.beginTransaction().replace(R.id.navDrawerFragmentContainer, fragment, tag).commit()
        mainDrawerLayout.closeDrawer(mainActivityNavigationView)
    }

    private fun initAds() {
        MobileAds.initialize(this) { }
        createAndLoadMainBannerAd()
    }

    private fun createAndLoadMainBannerAd() {
        val adRequest = AdRequest.Builder().build()
        adMainBannerView.loadAd(adRequest)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CURRENT_FRAGMENT_SAVED_STATE, currentFragment)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getString(CURRENT_FRAGMENT_SAVED_STATE)?.let {
            if (it.isNotEmpty()) {
                currentFragment = it
            }
        }
    }

    /** DrawerView code **/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // The action bar home/up action should open or close the drawer.
        when (item.itemId) {
            android.R.id.home -> {
                handleToolbarHomeClick()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleToolbarHomeClick() {
        mainDrawerLayout.apply {
            if (isDrawerOpen(mainActivityNavigationView)) {
                closeDrawer(mainActivityNavigationView)
            } else {
                openDrawer(mainActivityNavigationView)
            }
        }
    }

    private fun setupDrawerViewListener() {
        val drawerLayout = findViewById<DrawerLayout>(R.id.mainDrawerLayout)
        ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close).apply {
            drawerLayout.addDrawerListener(this)
            syncState()
        }
    }

    private fun setupNavigationView() {
        Log.d(tag, "AccelerometerSensorController.doesSensorExist() == ${AccelerometerSensorController.doesSensorExist()}")
        if (!AccelerometerSensorController.doesSensorExist()) {
            mainActivityNavigationView.menu.removeItem(R.id.accelerometerMenuItem)
        }
        Log.d(tag, "GyroscopeSensorController.doesSensorExist() == ${GyroscopeSensorController.doesSensorExist()}")
        if (!GyroscopeSensorController.doesSensorExist()) {
            mainActivityNavigationView.menu.removeItem(R.id.gyroscopeMenuItem)
        }
        Log.d(tag, "HeartbeatSensorController.doesSensorExist() == ${HeartbeatSensorController.doesSensorExist()}")
        if (!HeartbeatSensorController.doesSensorExist()) {
            mainActivityNavigationView.menu.removeItem(R.id.heartbeatSensorMenuItem)
        }
        Log.d(tag, "LinearAccelerationSensorController.doesSensorExist() == ${LinearAccelerationSensorController.doesSensorExist()}")
        if (!LinearAccelerationSensorController.doesSensorExist()) {
            mainActivityNavigationView.menu.removeItem(R.id.linearAccelerationSensorMenuItem)
        }
        Log.d(tag, "LightSensorController.doesSensorExist() == ${LightSensorController.doesSensorExist()}")
        if (!LightSensorController.doesSensorExist()) {
            mainActivityNavigationView.menu.removeItem(R.id.lightSensorMenuItem)
        }
        Log.d(tag, "MagneticFieldSensorController.doesSensorExist() == ${MagneticFieldSensorController.doesSensorExist()}")
        if (!MagneticFieldSensorController.doesSensorExist()) {
            mainActivityNavigationView.menu.removeItem(R.id.magneticFieldSensorMenuItem)
        }
        Log.d(tag, "RotationVectorController.doesSensorExist() == ${RotationVectorController.doesSensorExist()}")
        if (!RotationVectorController.doesSensorExist()) {
            mainActivityNavigationView.menu.removeItem(R.id.rotationVectorMenuItem)
        }
        Log.d(tag, "ProximitySensorController.doesSensorExist() == ${ProximitySensorController.doesSensorExist()}")
        if (!ProximitySensorController.doesSensorExist()) {
            mainActivityNavigationView.menu.removeItem(R.id.proximitySensorMenuItem)
        }

        setupNavigationViewListener()
    }

    private fun setupNavigationViewListener() {
        val navigationView = findViewById<NavigationView>(R.id.mainActivityNavigationView)
        navigationView.setNavigationItemSelectedListener(MyNavigationItemSelectedListener(::switchFragment))
    }
}