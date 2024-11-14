package com.example.logifitappp.data.models.commons

import com.example.logifitappp.core.wearebles.WearableActivityProvider
import com.example.logifitappp.core.wearebles.WearableActivityTypeEnum

open class WearableRawActivityModel(
    open var heartRate: Int,
    open var intensity: Int,
    open var steps: Int,
    open var timestamp: Long,
    open var type: Int,
    open var wearableId: Int,

    var provider: WearableActivityProvider<out WearableRawActivityModel>? = null
) {
    fun getNormalizedIntensity() = provider?.normalizeIntensity(intensity) ?: 0f

    fun getNormalizedType() = provider?.normalizeType(type) ?: WearableActivityTypeEnum.NOT_WORN

    fun isHeartRateValid() = heartRate in 10..250

    fun isSleep(): Boolean {
        return when (getNormalizedType()) {
            WearableActivityTypeEnum.REM_SLEEP,
            WearableActivityTypeEnum.DEEP_SLEEP,
            WearableActivityTypeEnum.LIGHT_SLEEP -> true

            else -> false
        }
    }
}
