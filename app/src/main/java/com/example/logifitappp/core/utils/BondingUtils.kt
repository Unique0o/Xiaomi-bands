package com.example.logifitappp.core.utils

import com.example.logifitappp.core.App
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableCandidate
import com.example.logifitappp.core.wearebles.WearableHelper

class BondingUtils {
    companion object {
        private fun connectThenComplete(candidate: WearableCandidate) {
            connectThenComplete(WearableHelper.getInstance().getSupportedWearable(candidate))
        }

        private fun connectThenComplete(wearable: Wearable) {
            App.getWearableServiceTo(wearable).disconnect()
            App.getWearableServiceTo(wearable).connect(true)
        }

        fun handleDeviceBonded(candidate: WearableCandidate?) {
            if (candidate == null) {
                println("candidate was null! Can't handle bonded device!")

                return
            }

            connectThenComplete(candidate)
        }
    }
}