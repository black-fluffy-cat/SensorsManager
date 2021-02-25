package com.fluffycat.sensorsmanager.activities

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.fluffycat.sensorsmanager.BuildConfig
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.ad.AdManager
import com.fluffycat.sensorsmanager.fragments.BaseChartFragment
import com.fluffycat.sensorsmanager.fragments.SENSOR_TYPE_ARG_NAME
import com.fluffycat.sensorsmanager.navigation_view.MyNavigationItemSelectedListener
import com.fluffycat.sensorsmanager.sensors.SensorType
import com.fluffycat.sensorsmanager.sensors.SensorTypeProvider
import com.fluffycat.sensorsmanager.utils.LogFlurryEvent
import com.fluffycat.sensorsmanager.utils.doesSensorExist
import com.fluffycat.sensorsmanager.utils.tag
import com.github.mikephil.charting.utils.Utils
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {

    companion object {
        const val CURRENT_FRAGMENT_SAVED_STATE = "currentFragment"
    }

    val mainViewModel: MainViewModel by viewModels()

    private var currentFragment: String = ""
    private lateinit var adRequest: AdRequest
    private lateinit var mInterstitialAd: InterstitialAd
    private val adManager = AdManager()
    private val interstitialAdCallback: () -> Unit = {
        if (mInterstitialAd.isLoaded) {
            LogFlurryEvent("Showing mInterstitialAd")
            mInterstitialAd.show()
        } else {
            LogFlurryEvent("mInterstitialAd not loaded yet")
            Log.d(tag, "The interstitial wasn't loaded yet.")
        }
    }
    private val sensorTypeProvider: SensorTypeProvider by inject()

    fun onDrawerItemSelected(fragment: Fragment) {
        switchFragment(fragment)
        adManager.onMenuItemClicked()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initAds()
        Utils.init(this) // For first chart to have proper lines size
        setupDrawerViewListener()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupNavigationView()
        setCurrentFragment(savedInstanceState)

        LogFlurryEvent("MainActivity onCreate")
    }

    private fun setCurrentFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val fragment = BaseChartFragment()
            val args = Bundle()
            args.putInt(SENSOR_TYPE_ARG_NAME, SensorType.Accelerometer.type)
            fragment.arguments = args
            switchFragment(fragment)
        }
    }

    private fun switchFragment(fragment: Fragment) {
        val tag: String = fragment::class.java.simpleName
        LogFlurryEvent("switchFragment to $tag")
        currentFragment = fragment.tag ?: ""
        supportFragmentManager.beginTransaction().replace(R.id.navDrawerFragmentContainer, fragment, tag).commit()
        mainDrawerLayout.closeDrawer(mainActivityNavigationView)
    }

    private fun initAds() {
        MobileAds.initialize(this) { }

        adRequest = adManager.createAdRequest()
        createAndLoadMainBannerAd()
        createAndLoadMainInterstitialAd()
    }

    private fun createAndLoadMainBannerAd() {
        adMainBannerView.loadAd(adRequest)
    }

    private fun createAndLoadMainInterstitialAd() {
        mInterstitialAd = InterstitialAd(this).apply {
            adListener = object : AdListener() {
                override fun onAdClosed() {
                    loadAd(adRequest)
                }
            }
            adUnitId = adManager.getInterstitialAdUnitId(BuildConfig.DEBUG)
            loadAd(adRequest)
        }
        adManager.registerInterstitialAdCallback(interstitialAdCallback)
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
        sensorTypeProvider.getMenuItemsAndCorrespondingSensors().forEach {
            if (!doesSensorExist(this, it.key)) {
                mainActivityNavigationView.menu.removeItem(it.value.type)
            }
        }
        setupNavigationViewListener()
    }

    private fun setupNavigationViewListener() {
        mainActivityNavigationView.setNavigationItemSelectedListener(
                MyNavigationItemSelectedListener(this, sensorTypeProvider))
    }
}