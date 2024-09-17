package com.example.logifitappp.core.wearebles.unknown

import com.example.logifitappp.core.wearebles.AbstractWearableSupport

class UnknownWearableSupport: AbstractWearableSupport() {
    override fun connect() = false

    override fun dispose() {

    }

    override fun useAutoConnect() = false
}