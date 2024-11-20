package com.example.logifitappp.core.wearebles

import com.example.logifitappp.core.wearebles.huami.miband.miband7.MiBand7Coordinator
import com.example.logifitappp.core.wearebles.unknown.UnknownWearableCoordinator
import com.example.logifitappp.core.wearebles.xiaomi.miband8.MiBand8Coordinator
import com.example.logifitappp.core.wearebles.xiaomi.miband9.MiBand9Coordinator

enum class WearableTypeEnum(private val coordinatorClass: Class<out WearableCoordinator>) {
    UNKNOWN(UnknownWearableCoordinator::class.java),
    MIBAND7(MiBand7Coordinator::class.java),
    MIBAND8(MiBand8Coordinator::class.java),
    MIBAND9(MiBand9Coordinator::class.java);

    fun getWearableCoordinator(): WearableCoordinator {
        return coordinatorClass.getDeclaredConstructor().newInstance()
    }

    fun isSupported(): Boolean {
        return this !== UNKNOWN
    }

    companion object {
        fun fromName(name: String): WearableTypeEnum {
            entries.forEach {
                if (it.name == name) return it
            }

            return UNKNOWN
        }
    }
}