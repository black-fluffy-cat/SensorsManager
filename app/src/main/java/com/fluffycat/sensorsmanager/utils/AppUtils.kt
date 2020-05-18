package com.fluffycat.sensorsmanager.utils

import android.content.Context
import android.widget.Toast

private const val LOG_TAG_MAX_LENGTH = 23

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

val Any.tag: String get() = this.javaClass.simpleName.take(LOG_TAG_MAX_LENGTH)

fun getLicencesInfoString() = ""