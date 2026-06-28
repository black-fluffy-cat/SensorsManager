package com.fluffycat.sensorsmanager.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AppUtilsTest {

    @Test
    fun `ifIsEmpty returns the fallback for an empty string`() {
        assertEquals("fallback", "" ifIsEmpty "fallback")
    }

    @Test
    fun `ifIsEmpty returns the original value for a non-empty string`() {
        assertEquals("original", "original" ifIsEmpty "fallback")
    }

    @Test
    fun `ifNotEmpty does not invoke the block for an empty string`() {
        var invoked = false
        "" ifNotEmpty { invoked = true }
        assertFalse(invoked)
    }

    @Test
    fun `ifNotEmpty invokes the block for a non-empty string`() {
        var invoked = false
        "value" ifNotEmpty { invoked = true }
        assertTrue(invoked)
    }

    @Test
    fun `tag is the simple class name truncated to the Android log tag limit`() {
        val owner = ClassWithAVeryLongNameThatExceedsTheAndroidLogTagLengthLimit()
        val expected = "ClassWithAVeryLongNameThatExceedsTheAndroidLogTagLengthLimit".take(23)
        assertEquals(23, owner.tag.length)
        assertEquals(expected, owner.tag)
    }

    @Test
    fun `tag of a short class name is left untouched`() {
        assertEquals("Sample", Sample().tag)
    }

    private class ClassWithAVeryLongNameThatExceedsTheAndroidLogTagLengthLimit

    private class Sample
}
