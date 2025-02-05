package com.example.logifitappp.core.wearebles.huami.miband.miband4

import com.example.logifitappp.enums.BondingStyleEnum
import com.example.logifitappp.core.wearebles.huami.HuamiConst
import com.example.logifitappp.core.wearebles.huami.HuamiCoordinator
import java.util.regex.Pattern

class MiBand4Coordinator: HuamiCoordinator() {
    override fun getBondingStyle() = BondingStyleEnum.BONDING_STYLE_REQUIRE_KEY

    override fun getSupportedWearableName(): Pattern? = Pattern.compile(HuamiConst.MI_BAND4_NAME, Pattern.CASE_INSENSITIVE)

    override fun getWearableSupportClass() = MiBand4Support::class.java

    override fun supportsHeartRateMeasurement() = true
}