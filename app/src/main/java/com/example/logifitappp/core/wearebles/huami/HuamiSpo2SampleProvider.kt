package com.example.logifitappp.core.wearebles.huami

import com.example.logifitappp.core.App
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.AbstractWearableSpo2SampleProvider
import com.example.logifitappp.data.models.HuamiSpo2SampleModel

class HuamiSpo2SampleProvider(wearable: Wearable): AbstractWearableSpo2SampleProvider<HuamiSpo2SampleModel>(wearable) {
    override fun getWearableSpo2SampleDao() = App.database.huamiSpo2SampleDao()
}