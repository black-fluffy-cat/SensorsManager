package com.fluffycat.sensorsmanager

import android.app.Application
import android.content.Context
import android.util.Log
import com.fluffycat.sensorsmanager.preferences.PreferencesManager
import com.fluffycat.sensorsmanager.utils.tag
import com.flurry.android.FlurryAgent
import com.flurry.android.FlurryPerformance

class SensorsManagerApplication : Application() {

    companion object {
        lateinit var instance: SensorsManagerApplication
            private set
        fun getContext(): Context = instance.applicationContext
    }

    val preferencesManager by lazy { PreferencesManager() }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Log.d(tag, "onCreate")

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
}