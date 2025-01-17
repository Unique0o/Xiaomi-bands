package com.example.logifitappp.core.wearebles.xiaomi

import com.example.logifitappp.enums.BondingStyleEnum
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.AbstractBleWearableCoordinator
import java.util.regex.Pattern

abstract class XiaomiCoordinator: AbstractBleWearableCoordinator() {
    fun checkDecryptionMac() = true

    override fun getActivityProvider(wearable: Wearable) = XiaomiActivityProvider(wearable)

    override fun getBondingStyle() = BondingStyleEnum.BONDING_STYLE_REQUIRE_KEY

    override fun getSpo2SampleProvider(wearable: Wearable) = XiaomiSpo2SampleProvider(wearable)

    override fun getStressSampleProvider(wearable: Wearable) = XiaomiStressSampleProvider(wearable)

    override fun getWearableSupportClass() = XiaomiSupport::class.java

    override fun isAuthenticationKeyValid(authenticationKey: String): Boolean {
        val authenticationKeyBytes = authenticationKey.trim().toByteArray()

        return authenticationKeyBytes.size == 32
                || (authenticationKey.startsWith("0x") && authenticationKeyBytes.size == 34)
                || AUTH_KEY_PATTERN.matcher(authenticationKey.trim()).matches()
    }

    override fun supportsActivityDataFetching() = true

    override fun supportsActivityTracking() = true

    override fun supportsHeartRateMeasurement() = true

    override fun supportsRemSleep() = true

    override fun supportsSpo2() = true

    override fun supportsStressMeasurement() = true

    companion object {
        val AUTH_KEY_PATTERN: Pattern = Pattern.compile("^[0-9]+\$")
    }
}