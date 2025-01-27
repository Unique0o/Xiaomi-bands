package com.example.logifitappp.core.wearebles.huami

import com.example.logifitappp.core.App
import com.example.logifitappp.core.wearebles.AbstractWearableSampleProvider
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.data.models.HuamiStressSampleModel

class HuamiStressSampleProvider(wearable: Wearable): AbstractWearableSampleProvider<HuamiStressSampleModel>(wearable) {
    override fun getWearableSampleDao() = App.database.huamiStressSampleDao()
}