package com.example.logifitappp.core.wearebles.huami

import com.example.logifitappp.core.App
import com.example.logifitappp.core.Preferences
import com.example.logifitappp.core.wearebles.AbstractBleWearableCoordinator
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableActivityProvider
import com.example.logifitappp.core.wearebles.huami.miband.miband2.MiBand2ActivityProvider
import com.example.logifitappp.data.models.HuamiExtendedRawActivityModel

abstract class HuamiCoordinator: AbstractBleWearableCoordinator() {
    override fun getActivityProvider(wearable: Wearable): WearableActivityProvider<HuamiExtendedRawActivityModel> = MiBand2ActivityProvider(wearable)

    override fun getSpo2SampleProvider(wearable: Wearable) = HuamiSpo2SampleProvider(wearable)

    override fun suggestUnbindBeforePair() = false

    override fun supportsActivityDataFetching() = true

    companion object {
        fun getKeepActivityDataOnDevice(mac: String): Boolean {
            val prefs = Preferences(App.getWearableSpecificSharedPrefs(mac)!!)
            return prefs.getBoolean("keep_activity_data_on_device", false)
        }
    }
}