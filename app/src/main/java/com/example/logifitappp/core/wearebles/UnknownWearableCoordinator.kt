package com.example.logifitappp.core.wearebles

import java.util.regex.Pattern

class UnknownWearableCoordinator: WearableCoordinator() {
    override fun getSupportedWearableName(): Pattern? {
        return null
    }

    override fun getWearableSupportClass(): Class<out WearableSupport> {
        TODO("Not yet implemented")
    }
}