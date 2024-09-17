package com.example.logifitappp.core.wearebles.unknown

import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableCoordinator
import java.util.regex.Pattern

class UnknownWearableCoordinator: WearableCoordinator() {
    override fun getActivityProvider(wearable: Wearable) = UnknownActivityProvider(wearable)

    override fun getSupportedWearableName(): Pattern? {
        return null
    }

    override fun getWearableSupportClass() = UnknownWearableSupport::class.java
}