package com.example.logifitappp.core.wearebles.huami.zeppos

import com.example.logifitappp.enums.BondingStyleEnum
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.huami.HuamiCoordinator
import com.example.logifitappp.core.wearebles.huami.HuamiExtendedActivityProvider
import java.util.regex.Pattern

abstract class ZeppOsCoordinator: HuamiCoordinator() {
    override fun getActivityProvider(wearable: Wearable) = HuamiExtendedActivityProvider(wearable)

    override fun getBondingStyle() = BondingStyleEnum.BONDING_STYLE_REQUIRE_KEY

    override fun getSupportedWearableName(): Pattern? {
        return Pattern.compile("^${getWearableBluetoothName()}([- ][A-Z0-9]{4})?\$")
    }

    abstract fun getWearableBluetoothName(): String

    override fun getWearableSupportClass() = ZeppOsSupport::class.java

    override fun isAuthenticationKeyValid(authenticationKey: String): Boolean {
        val authenticationKeyBytes = authenticationKey.trim().toByteArray()

        return authenticationKeyBytes.size == 32 || (authenticationKey.trim().startsWith("0x") && authenticationKeyBytes.size == 34)
    }

    override fun supportsHeartRateMeasurement() = true

    override fun supportsRemSleep() = true

    override fun supportsSpo2() = true
}