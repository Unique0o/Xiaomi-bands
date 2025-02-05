package com.example.logifitappp.core.wearebles.xiaomi

import com.example.logifitappp.core.wearebles.AbstractActivityToSampleProvider
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.data.models.XiaomiRawActivityModel
import com.example.logifitappp.data.models.commons.WearableStressSampleModel

class XiaomiStressSampleProvider(wearable: Wearable): AbstractActivityToSampleProvider<XiaomiStressSampleProvider.XiaomiStressSample, XiaomiRawActivityModel>(wearable) {
    data class XiaomiStressSample(
        override var stress: Int,
        override var timestamp: Long,
        override var wearableId: Int
    ): WearableStressSampleModel(stress, timestamp, wearableId)

    override fun convertActivity(activity: XiaomiRawActivityModel): XiaomiStressSample? {
        if (activity.stress == null || activity.stress == 0) return null

        return XiaomiStressSample(
            stress = activity.stress!!,
            timestamp = activity.timestamp * 1000,
            wearableId = activity.wearableId
        )
    }
}