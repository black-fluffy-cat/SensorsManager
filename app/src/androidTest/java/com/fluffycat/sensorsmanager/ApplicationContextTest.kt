package com.fluffycat.sensorsmanager

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ApplicationContextTest {

    @Test
    fun usesTheExpectedApplicationId() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.fluffycat.sensorsmanager", context.packageName)
    }
}
