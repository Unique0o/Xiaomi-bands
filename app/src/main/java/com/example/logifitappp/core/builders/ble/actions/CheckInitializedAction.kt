package com.example.logifitappp.core.builders.ble.actions

import com.example.logifitappp.core.wearebles.Wearable

class CheckInitializedAction(private val wearable: Wearable): AbortTransactionAction() {
    override fun shouldAbort(): Boolean = when (wearable.isInitialized()) {
        true -> true.also { println("Aborting device initialization, because already initialized: $wearable") }
        false -> false
    }
}