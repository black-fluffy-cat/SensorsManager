package com.fluffycat.sensorsmanager.ad

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AdManagerTest {

    @Test
    fun `interstitial callback is invoked on every third menu click`() {
        val adManager = AdManager()
        var invocations = 0
        adManager.registerInterstitialAdCallback { invocations++ }

        adManager.onMenuItemClicked()
        adManager.onMenuItemClicked()
        assertEquals(0, invocations)

        adManager.onMenuItemClicked()
        assertEquals(1, invocations)

        repeat(3) { adManager.onMenuItemClicked() }
        assertEquals(2, invocations)
    }

    @Test
    fun `unregistering the callback stops further invocations`() {
        val adManager = AdManager()
        var invocations = 0
        adManager.registerInterstitialAdCallback { invocations++ }
        adManager.unregisterInterstitialAdCallback()

        repeat(6) { adManager.onMenuItemClicked() }

        assertEquals(0, invocations)
    }

    @Test
    fun `interstitial ad unit id depends on the build type`() {
        val adManager = AdManager()

        assertEquals("ca-app-pub-3940256099942544/1033173712", adManager.getInterstitialAdUnitId(isDebug = true))
        assertEquals("ca-app-pub-7809340407306359/4753873001", adManager.getInterstitialAdUnitId(isDebug = false))
    }
}
