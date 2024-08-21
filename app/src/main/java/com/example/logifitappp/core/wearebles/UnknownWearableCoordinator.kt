package com.example.logifitappp.core.wearebles

import java.util.regex.Pattern

class UnknownWearableCoordinator: WearableCoordinator() {
    override fun getSupportedDeviceName(): Pattern? {
        return null
    }
}