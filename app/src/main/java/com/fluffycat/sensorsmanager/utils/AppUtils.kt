package com.fluffycat.sensorsmanager.utils

import android.content.Context
import android.widget.Toast
import com.flurry.android.FlurryAgent

const val HEART_RATE_REQUEST_CODE = 1
const val REQUEST_RECORD_AUDIO_REQUEST_CODE = 2

private const val LOG_TAG_MAX_LENGTH = 23

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

val Any.tag: String get() = this.javaClass.simpleName.take(LOG_TAG_MAX_LENGTH)

fun getLicencesInfoString() =
    "Icon made by srip, cursor-creative, flat-icons, freepik, smashicons from www.flaticon.com"

fun LogFlurryEvent(message: String) {
    FlurryAgent.logEvent(message)
}