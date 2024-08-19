package com.example.logifitappp.core.wearebles

import com.example.logifitappp.core.wearebles.xiaomi.miband8.MiBand8Coordinator

enum class WearableType(private val coordinatorClass: Class<out WearableCoordinator>) {
    UNKNOWN(UnknownWearableCoordinator::class.java),
    MIBAND8(MiBand8Coordinator::class.java);

    fun getWearableCoordinator(): WearableCoordinator {
        return coordinatorClass.getDeclaredConstructor().newInstance()
    }

    fun isSupported(): Boolean {
        return this !== UNKNOWN
    }
}