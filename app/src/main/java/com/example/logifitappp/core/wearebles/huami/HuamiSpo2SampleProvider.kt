package com.example.logifitappp.core.wearebles.huami

import com.example.logifitappp.core.App
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.AbstractWearableSampleProvider
import com.example.logifitappp.data.models.HuamiSpo2SampleModel

class HuamiSpo2SampleProvider(wearable: Wearable): AbstractWearableSampleProvider<HuamiSpo2SampleModel>(wearable) {
    override fun getWearableSampleDao() = App.database.huamiSpo2SampleDao()
}