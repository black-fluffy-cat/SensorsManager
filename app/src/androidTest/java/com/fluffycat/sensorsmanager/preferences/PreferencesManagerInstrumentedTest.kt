package com.fluffycat.sensorsmanager.preferences

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fluffycat.sensorsmanager.values.DistanceUnit
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PreferencesManagerInstrumentedTest {

    private val preferencesManager = PreferencesManager()

    @Test
    fun chosenDistanceUnitIsPersistedAcrossReads() {
        preferencesManager.saveChosenDistanceUnit(DistanceUnit.FEET)
        assertEquals(DistanceUnit.FEET, preferencesManager.readChosenDistanceUnit())

        preferencesManager.saveChosenDistanceUnit(DistanceUnit.METERS)
        assertEquals(DistanceUnit.METERS, preferencesManager.readChosenDistanceUnit())
    }
}
