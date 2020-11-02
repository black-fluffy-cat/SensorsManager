package com.fluffycat.sensorsmanager.ad

import com.google.android.gms.ads.AdRequest

class AdManager {

    companion object {
        private const val CLICKS_BEFORE_AD = 3
    }

    private var menuClicks = 0
    private var interstitialAdCallback: (() -> Unit)? = null

    fun createAdRequest(): AdRequest = AdRequest.Builder().build()

    @Suppress("SameParameterValue")
    fun getInterstitialAdUnitId(isDebug: Boolean): String =
        if (isDebug) {
            "ca-app-pub-3940256099942544/1033173712"
        } else {
            "ca-app-pub-7809340407306359/4753873001"
        }

    fun onMenuItemClicked() {
        if (++menuClicks == CLICKS_BEFORE_AD) {
            menuClicks = 0
            interstitialAdCallback?.invoke()
        }
    }

    fun registerInterstitialAdCallback(callback: () -> Unit) {
        interstitialAdCallback = callback
    }


    fun unregisterInterstitialAdCallback() {
        interstitialAdCallback = null
    }
}