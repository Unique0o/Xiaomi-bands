package com.example.logifitappp.core.wearebles.unknown

import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.AbstractWearableActivityProvider
import com.example.logifitappp.enums.WearableActivityTypeEnum
import com.example.logifitappp.data.models.commons.WearableRawActivityModel

class UnknownActivityProvider(wearable: Wearable): AbstractWearableActivityProvider<WearableRawActivityModel>(wearable) {
    override fun getWearableRawActivityDao() = null

    override fun normalizeIntensity(intensity: Int) = 0f

    override fun normalizeType(type: Int) = WearableActivityTypeEnum.UNKNOWN
}