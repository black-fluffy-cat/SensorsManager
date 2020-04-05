package com.fluffycat.sensorsmanager

import android.app.Application
import android.util.Log
import com.fluffycat.sensorsmanager.utils.tag

class SensorsManagerApplication : Application() {

    companion object {
        lateinit var instance: SensorsManagerApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Log.d(tag, "onCreate")
    }
}