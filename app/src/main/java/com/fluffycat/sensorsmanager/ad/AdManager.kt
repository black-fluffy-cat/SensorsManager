package com.fluffycat.sensorsmanager.ad

import com.google.android.gms.ads.AdRequest

class AdManager {

    fun createAdRequest(): AdRequest = AdRequest.Builder().build()

    @Suppress("SameParameterValue")
    fun getInterstitialAdUnitId(isDebug: Boolean): String =
        if (isDebug) {
            "ca-app-pub-3940256099942544/1033173712"
        } else {
            "ca-app-pub-7809340407306359/4753873001"
        }
}