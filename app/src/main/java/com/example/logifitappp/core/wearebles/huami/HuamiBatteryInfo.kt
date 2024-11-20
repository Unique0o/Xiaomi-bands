package com.example.logifitappp.core.wearebles.huami

import com.example.logifitappp.core.events.WearableBatteryInfoEvent
import com.example.logifitappp.core.wearebles.huami.miband.AbstractMiBandInfo

class HuamiBatteryInfo(payload: ByteArray): AbstractMiBandInfo(payload) {
    fun getLevelInPercentage(): Int {
        if (data.size >= 2) return data[1].toInt()

        return 50
    }

    fun toWearableEvent(): WearableBatteryInfoEvent {
        return WearableBatteryInfoEvent().apply {
            level = getLevelInPercentage()
        }
    }
}