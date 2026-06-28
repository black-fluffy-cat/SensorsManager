package com.fluffycat.sensorsmanager.activities

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.fluffycat.sensorsmanager.BuildConfig
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.ad.AdManager
import com.fluffycat.sensorsmanager.databinding.ActivityMainBinding
import com.fluffycat.sensorsmanager.fragments.BaseChartFragment
import com.fluffycat.sensorsmanager.fragments.SENSOR_TYPE_ARG_NAME
import com.fluffycat.sensorsmanager.navigation_view.MyNavigationItemSelectedListener
import com.fluffycat.sensorsmanager.sensors.SensorType
import com.fluffycat.sensorsmanager.sensors.SensorTypeProvider
import com.fluffycat.sensorsmanager.utils.LogFlurryEvent
import com.fluffycat.sensorsmanager.utils.doesSensorExist
import com.fluffycat.sensorsmanager.utils.tag
import com.github.mikephil.charting.utils.Utils
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {

    companion object {
        const val CURRENT_FRAGMENT_SAVED_STATE = "currentFragment"
    }

    val mainViewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    private var currentFragment: String = ""
    private lateinit var adRequest: AdRequest
    private var mInterstitialAd: InterstitialAd? = null
    private val adManager = AdManager()
    private val interstitialAdCallback: () -> Unit = {
        val interstitialAd = mInterstitialAd
        if (interstitialAd != null) {
            LogFlurryEvent("Showing mInterstitialAd")
            interstitialAd.show(this)
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
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        binding.mainDrawerLayout.closeDrawer(binding.mainActivityNavigationView)
    }

    private fun initAds() {
        MobileAds.initialize(this) { }

        adRequest = adManager.createAdRequest()
        createAndLoadMainBannerAd()
        createAndLoadMainInterstitialAd()
    }

    private fun createAndLoadMainBannerAd() {
        binding.adMainBannerView.loadAd(adRequest)
    }

    private fun createAndLoadMainInterstitialAd() {
        InterstitialAd.load(
                this,
                adManager.getInterstitialAdUnitId(BuildConfig.DEBUG),
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        mInterstitialAd = interstitialAd.apply {
                            fullScreenContentCallback = object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    mInterstitialAd = null
                                    createAndLoadMainInterstitialAd()
                                }
                            }
                        }
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        mInterstitialAd = null
                    }
                })
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
        binding.mainDrawerLayout.apply {
            if (isDrawerOpen(binding.mainActivityNavigationView)) {
                closeDrawer(binding.mainActivityNavigationView)
            } else {
                openDrawer(binding.mainActivityNavigationView)
            }
        }
    }

    private fun setupDrawerViewListener() {
        val drawerLayout = binding.mainDrawerLayout
        ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close).apply {
            drawerLayout.addDrawerListener(this)
            syncState()
        }
    }

    private fun setupNavigationView() {
        sensorTypeProvider.getMenuItemsAndCorrespondingSensors().forEach {
            if (!doesSensorExist(this, it.key)) {
                binding.mainActivityNavigationView.menu.removeItem(it.value.type)
            }
        }
        setupNavigationViewListener()
    }

    private fun setupNavigationViewListener() {
        binding.mainActivityNavigationView.setNavigationItemSelectedListener(
                MyNavigationItemSelectedListener(this, sensorTypeProvider))
    }
}
