package com.example.logifitappp.core.wearebles

import com.example.logifitappp.core.BondingStyleEnum
import com.example.logifitappp.core.bluetooth.ConnectionTypeEnum
import java.util.EnumSet
import java.util.regex.Pattern

abstract class WearableCoordinator {
    fun createDevice(candidate: WearableCandidate, wearableType: WearableTypeEnum): Wearable {
        return Wearable(candidate.getDevice().address, candidate.getName(), null, wearableType)
    }

    open fun getBondingStyle(): Int {
        return BondingStyleEnum.BONDING_STYLE_ASK
    }

    open fun getConnectionType(): ConnectionTypeEnum {
        return ConnectionTypeEnum.BOTH
    }

    fun getInitialFlags(): EnumSet<WearableSupportFlagEnum> = EnumSet.of(WearableSupportFlagEnum.BUSY_CHECKING)

    open fun isAuthenticationKeyValid(authenticationKey: String): Boolean {
        return !(authenticationKey.toByteArray().size < 34 || !authenticationKey.startsWith("0x"))
    }

    fun isConnectable(): Boolean {
        return true
    }

    fun supports(candidate: WearableCandidate): Boolean {
        val pattern = getSupportedWearableName()

        if (pattern == null) {
            println("$javaClass should either override getSupportedDeviceName or supports(GBDeviceCandidate)")
            return false
        }

        return pattern.matcher(candidate.getName()).matches()
    }

    abstract fun getSupportedWearableName(): Pattern?
    abstract fun getWearableSupportClass(): Class<out WearableSupport>
}