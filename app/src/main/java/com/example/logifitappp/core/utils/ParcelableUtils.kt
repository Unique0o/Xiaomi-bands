package com.example.logifitappp.core.utils

import android.content.Intent
import android.os.Build
import android.os.Build.VERSION
import android.os.Parcelable

inline fun <reified T: Parcelable> Intent.parcelableExtra(key: String): T? = when {
    VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

inline fun <reified T: Parcelable> Intent.parcelableArrayExtra(key: String): Array<out Parcelable>? = when {
    VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableArrayExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableArrayExtra(key)
}