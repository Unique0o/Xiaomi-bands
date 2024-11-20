package com.example.logifitappp.core.wearebles.huami.miband.miband7

import com.example.logifitappp.core.wearebles.huami.HuamiConst
import com.example.logifitappp.core.wearebles.huami.zeppos.ZeppOsCoordinator

class MiBand7Coordinator: ZeppOsCoordinator() {
    override fun getWearableBluetoothName() = HuamiConst.XIAOMI_SMART_BAND7_NAME
}