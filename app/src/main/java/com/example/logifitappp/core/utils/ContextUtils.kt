package com.example.logifitappp.core.utils

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import java.io.File
import java.util.Date

tailrec fun Context.findActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun Context.createImageFile(): File {
    val timestamp = DateTimeUtils.format(Date(), "yyyy_MM_dd_HH_mm_ss")
    val filename = "image_$timestamp.jpg"

    return File(this.cacheDir, filename).apply {
        parentFile?.mkdirs()
        deleteOnExit()
    }
}