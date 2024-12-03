package com.example.logifitappp.core.wearebles.fitpro

import com.example.logifitappp.core.BondingStyleEnum
import com.example.logifitappp.core.wearebles.AbstractBleWearableCoordinator
import com.example.logifitappp.core.wearebles.Wearable
import java.util.regex.Pattern

class FitProCoordinator: AbstractBleWearableCoordinator() {
    override fun getBondingStyle() = BondingStyleEnum.BONDING_STYLE_ASK

    override fun getActivityProvider(wearable: Wearable) = FitProActivityProvider(wearable)

    override fun getSupportedWearableName(): Pattern? = Pattern.compile("M6.*|M4.*|LH716|Sunset 6|Watch7|Fit1900|PROLINK.*|716")

    override fun getWearableSupportClass() = FitProSupport::class.java

    override fun supportsActivityDataFetching() = true

    override fun supportsHeartRateMeasurement() = true
}