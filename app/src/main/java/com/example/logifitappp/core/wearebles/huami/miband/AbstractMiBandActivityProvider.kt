package com.example.logifitappp.core.wearebles.huami.miband

import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableActivityProvider
import com.example.logifitappp.data.models.HuamiExtendedRawActivityModel

abstract class AbstractMiBandActivityProvider(wearable: Wearable): WearableActivityProvider<HuamiExtendedRawActivityModel>(wearable) {
    private val movement = 180.0f

    override fun normalizeIntensity(intensity: Int) = intensity / movement
}