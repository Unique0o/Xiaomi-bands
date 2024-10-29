package com.example.logifitappp.core.wearebles.xiaomi

import com.example.logifitappp.core.BondingStyleEnum
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableBLECoordinator
import java.util.regex.Pattern

abstract class XiaomiCoordinator: WearableBLECoordinator() {
    fun checkDecryptionMac() = true

    override fun getActivityProvider(wearable: Wearable) = XiaomiActivityProvider(wearable)

    override fun getBondingStyle(): Int {
        return BondingStyleEnum.BONDING_STYLE_REQUIRE_KEY
    }

    override fun getWearableSupportClass() = XiaomiSupport::class.java

    override fun isAuthenticationKeyValid(authenticationKey: String): Boolean {
        val authenticationKeyBytes = authenticationKey.trim().toByteArray()

        return authenticationKeyBytes.size == 32
                || (authenticationKey.startsWith("0x") && authenticationKeyBytes.size == 34)
                || AUTH_KEY_PATTERN.matcher(authenticationKey.trim()).matches()
    }

    override fun supportsHeartRateMeasurement() = true

    override fun supportsRemSleep() = true

    override fun supportsActivityDataFetching() = true

    companion object {
        val AUTH_KEY_PATTERN: Pattern = Pattern.compile("^[0-9]+\$")
    }
}