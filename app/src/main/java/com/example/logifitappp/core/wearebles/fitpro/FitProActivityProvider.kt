package com.example.logifitappp.core.wearebles.fitpro

import com.example.logifitappp.core.App
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableActivityProvider
import com.example.logifitappp.core.wearebles.WearableActivityTypeEnum
import com.example.logifitappp.data.models.FitProRawActivityModel

class FitProActivityProvider(wearable: Wearable): WearableActivityProvider<FitProRawActivityModel>(wearable) {
    override fun getWearableRawActivityDao() = App.database.fitProRawActivityDao()

    override fun normalizeIntensity(intensity: Int) = intensity / 2000f

    override fun normalizeType(type: Int) = when (type) {
        1 -> WearableActivityTypeEnum.ACTIVITY
        11 -> WearableActivityTypeEnum.DEEP_SLEEP
        12 -> WearableActivityTypeEnum.LIGHT_SLEEP
        else -> WearableActivityTypeEnum.UNKNOWN
    }
}