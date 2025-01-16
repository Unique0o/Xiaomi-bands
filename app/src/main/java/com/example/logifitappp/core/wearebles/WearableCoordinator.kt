package com.example.logifitappp.core.wearebles

import com.example.logifitappp.enums.BondingStyleEnum
import com.example.logifitappp.core.bluetooth.ConnectionTypeEnum
import com.example.logifitappp.data.models.commons.WearableRawActivityModel
import com.example.logifitappp.data.models.commons.WearableSpo2SampleModel
import com.example.logifitappp.enums.WearableSupportFlagEnum
import java.util.EnumSet
import java.util.regex.Pattern

abstract class WearableCoordinator {
    fun createWearable(candidate: WearableCandidate, wearableType: WearableTypeEnum): Wearable {
        return Wearable(candidate.getDevice().address, candidate.getName(), null, wearableType)
    }

    open fun getBondingStyle(): BondingStyleEnum {
        return BondingStyleEnum.BONDING_STYLE_ASK
    }

    open fun getConnectionType(): ConnectionTypeEnum {
        return ConnectionTypeEnum.BOTH
    }

    open fun getInitialFlags(): EnumSet<WearableSupportFlagEnum> = EnumSet.of(WearableSupportFlagEnum.BUSY_CHECKING)

    open fun getSpo2SampleProvider(wearable: Wearable): WearableSpo2SampleProvider<out WearableSpo2SampleModel>? = null

    open fun isAuthenticationKeyValid(authenticationKey: String): Boolean {
        return !(authenticationKey.toByteArray().size < 34 || !authenticationKey.startsWith("0x"))
    }

    fun isConnectable(): Boolean {
        return true
    }

    open fun suggestUnbindBeforePair(): Boolean {
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

    open fun supportsActivityDataFetching() = false

    open fun supportsActivityTracking() = false

    open fun supportsHeartRateMeasurement() = false

    open fun supportsRemSleep() = false

    open fun supportsSpo2() = false

    open fun supportsStepCounter() = supportsActivityTracking()

    abstract fun getActivityProvider(wearable: Wearable): WearableActivityProvider<out WearableRawActivityModel>
    abstract fun getSupportedWearableName(): Pattern?
    abstract fun getWearableSupportClass(): Class<out WearableSupport>
}