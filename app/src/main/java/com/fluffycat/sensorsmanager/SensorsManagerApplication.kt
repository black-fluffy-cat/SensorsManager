package com.fluffycat.sensorsmanager

import android.app.Application
import android.content.Context
import android.util.Log
import com.fluffycat.sensorsmanager.koin.smMainModule
import com.fluffycat.sensorsmanager.preferences.PreferencesManager
import com.fluffycat.sensorsmanager.utils.tag
import com.flurry.android.FlurryAgent
import com.flurry.android.FlurryPerformance
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


// TODO Switch to Navigation components
// TODO Extract drawer code from MainActivity
// TODO Refactor CollectingDataService - start collecting on action received, not on onCreate
// TODO Integrate with SonarCloud
// TODO Add ViewModels
class SensorsManagerApplication : Application() {

    companion object {
        lateinit var instance: SensorsManagerApplication
            private set

        fun getContext(): Context = instance.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Log.d(tag, "onCreate")
        launchKoin()
        if (!BuildConfig.DEBUG) {
            prepareFlurryMonitoring()
        }
    }

    private fun prepareFlurryMonitoring() {
        val flurryKey = "DJ5H5SH83XRTMC4D2GW3"

        FlurryAgent.Builder()
            .withDataSaleOptOut(false) //CCPA - the default value is false
            .withCaptureUncaughtExceptions(true)
            .withIncludeBackgroundSessionsInMetrics(true)
            .withLogLevel(Log.VERBOSE)
            .withPerformanceMetrics(FlurryPerformance.ALL)
            .withLogEnabled(true)
            .build(this, flurryKey)
    }

    private fun launchKoin() {
        startKoin {
            androidLogger()
            androidContext(this@SensorsManagerApplication)
            modules(smMainModule)
        }
    }
}