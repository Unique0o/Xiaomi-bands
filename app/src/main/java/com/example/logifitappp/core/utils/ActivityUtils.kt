package com.example.logifitappp.core.utils

import android.app.Activity
import android.content.pm.ActivityInfo

fun Activity.enterFullscreenMode() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
}

fun Activity.exitFullscreenMode() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
}