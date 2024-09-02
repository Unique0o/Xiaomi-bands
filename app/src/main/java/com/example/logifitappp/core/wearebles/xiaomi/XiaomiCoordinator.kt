package com.example.logifitappp.core.wearebles.xiaomi

import com.example.logifitappp.core.BondingStyleEnum
import com.example.logifitappp.core.wearebles.WearableBLECoordinator
import com.example.logifitappp.core.wearebles.WearableSupport
import java.util.regex.Pattern

abstract class XiaomiCoordinator: WearableBLECoordinator() {
    fun checkDecryptionMac() = true

    override fun getBondingStyle(): Int {
        return BondingStyleEnum.BONDING_STYLE_REQUIRE_KEY
    }

    override fun getWearableSupportClass(): Class<out WearableSupport> {
        return XiaomiSupport::class.java
    }

    override fun isAuthenticationKeyValid(authenticationKey: String): Boolean {
        val authenticationKeyBytes = authenticationKey.trim().toByteArray()

        return authenticationKeyBytes.size == 32
                || (authenticationKey.startsWith("0x") && authenticationKeyBytes.size == 34)
                || AUTH_KEY_PATTERN.matcher(authenticationKey.trim()).matches()
    }

    companion object {
        val AUTH_KEY_PATTERN: Pattern = Pattern.compile("^[0-9]+\$")
    }
}