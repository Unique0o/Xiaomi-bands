package com.example.logifitappp.core.wearebles.xiaomi

import com.example.logifitappp.core.wearebles.AbstractActivityToSpo2SampleProvider
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.data.models.XiaomiRawActivityModel
import com.example.logifitappp.data.models.commons.WearableSpo2SampleModel
import com.example.logifitappp.enums.Spo2ModeEnum

class XiaomiSpo2SampleProvider(wearable: Wearable): AbstractActivityToSpo2SampleProvider<XiaomiSpo2SampleProvider.XiaomiSpo2SampleModel, XiaomiRawActivityModel>(wearable) {
    data class XiaomiSpo2SampleModel(
        override var modeName: String,
        override var spo2: Int,
        override var timestamp: Long,
        override var wearableId: Int
    ): WearableSpo2SampleModel(modeName, spo2, timestamp, wearableId)

    override fun convertActivity(activity: XiaomiRawActivityModel): XiaomiSpo2SampleModel? {
        if (activity.spo2 == null || activity.spo2 == 0) return null

        return XiaomiSpo2SampleModel(
            modeName = (if (activity.isSleep()) Spo2ModeEnum.ASLEEP else Spo2ModeEnum.AWAKE).name,
            spo2 = activity.spo2!!,
            timestamp = activity.timestamp * 1000,
            wearableId = activity.wearableId
        )
    }
}