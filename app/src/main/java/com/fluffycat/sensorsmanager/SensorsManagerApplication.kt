package com.fluffycat.sensorsmanager

import android.app.Application
import android.content.Context
import android.util.Log
import com.fluffycat.sensorsmanager.koin.smMainModule
import com.fluffycat.sensorsmanager.preferences.PreferencesManager
import com.fluffycat.sensorsmanager.utils.tag
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
    }

    private fun launchKoin() {
        startKoin {
            androidLogger()
            androidContext(this@SensorsManagerApplication)
            modules(smMainModule)
        }
    }
}