package com.example.logifitappp.core.wearebles

import java.util.regex.Pattern

abstract class WearableCoordinator {
    fun createDevice(candidate: WearableCandidate, wearableType: WearableType): Wearable {
        return Wearable(candidate.getDevice().address, candidate.getName(), null, wearableType)
    }

    fun supports(candidate: WearableCandidate): Boolean {
        val pattern = getSupportedDeviceName()

        if (pattern == null) {
            println("$javaClass should either override getSupportedDeviceName or supports(GBDeviceCandidate)")
            return false
        }

        return pattern.matcher(candidate.getName()).matches()
    }

    abstract fun getSupportedDeviceName(): Pattern?
}