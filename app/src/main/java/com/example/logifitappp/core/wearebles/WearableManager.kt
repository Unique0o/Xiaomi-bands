package com.example.logifitappp.core.wearebles

import android.content.Context
import java.util.Collections

class WearableManager(private val context: Context) {
    val wearables = mutableListOf<Wearable>()
        get() = Collections.unmodifiableList(field)
}