package com.example.logifitappp.core.wearebles.huami.miband.amazfitbip

import com.example.logifitappp.core.builders.ble.TransactionBuilder
import com.example.logifitappp.core.wearebles.huami.HuamiSupport

open class AmazfitBipSupport: HuamiSupport() {
    override fun phase2Initialize(builder: TransactionBuilder) {
        super.phase2Initialize(builder)
        println("phase2Initialize...")

        requestGPSVersion(builder)
    }
}