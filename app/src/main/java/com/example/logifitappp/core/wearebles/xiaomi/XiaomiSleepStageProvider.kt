package com.example.logifitappp.core.wearebles.xiaomi

import com.example.logifitappp.core.App
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableTimeProvider
import com.example.logifitappp.data.models.XiaomiSleepStageModel

class XiaomiSleepStageProvider(wearable: Wearable): WearableTimeProvider<XiaomiSleepStageModel>(wearable) {
    override fun getWearableTimeDao() = App.database.xiaomiSleepStageDao()
}