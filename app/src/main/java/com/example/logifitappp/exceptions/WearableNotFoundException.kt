package com.example.logifitappp.exceptions

import com.example.logifitappp.core.wearebles.Wearable

class WearableNotFoundException: Exception {
    private val address: String?

    override val message: String
        get() = "device $address not found cached"

    constructor(wearable: Wearable) {
        address = wearable.getAddress()
    }

    constructor(address: String?) {
        this.address = address
    }
}