package com.example.logifitappp.core.wearebles.huami.miband

import com.example.logifitappp.core.builders.ble.TransactionBuilder
import com.example.logifitappp.core.builders.ble.actions.Action
import com.example.logifitappp.core.utils.GattCharacteristic
import com.example.logifitappp.core.wearebles.AbstractBleWearableSupport
import com.example.logifitappp.core.wearebles.SimpleNotification
import com.example.logifitappp.enums.VibrationTypeEnum
import kotlin.math.max
import kotlin.math.min

open class V2NotificationStrategy<T: AbstractBleWearableSupport>(protected val support: T): NotificationStrategy {
    open protected fun sendCustomNotification(vibrationProfile: VibrationProfile, simpleNotification: SimpleNotification?, action: Action?, builder: TransactionBuilder) {
        val alert = support.getCharacteristic(GattCharacteristic.UUID_CHARACTERISTIC_ALERT_LEVEL)

        for (i in 0 until vibrationProfile.repeat) {
            val onOffSequence = vibrationProfile.onOffSequence
            var j = 0

            while (j < onOffSequence.size) {
                builder.write(alert, byteArrayOf(GattCharacteristic.MILD_ALERT))
                builder.wait(min(onOffSequence[j], 500))
                builder.write(alert, byteArrayOf(GattCharacteristic.NO_ALERT))

                if (++j < onOffSequence.size) builder.wait(max(onOffSequence[j], 25))

                action?.let { builder.add(it) }
                j++
            }
        }
    }

    override fun sendCustomNotification(
        vibrationProfile: VibrationProfile,
        simpleNotification: SimpleNotification?,
        flashTimes: Int,
        flashColour: Int,
        originalColour: Int,
        flashDuration: Long,
        action: Action,
        builder: TransactionBuilder
    ) {
        sendCustomNotification(vibrationProfile, simpleNotification, action, builder)
    }

    override fun sendDefaultNotification(
        builder: TransactionBuilder,
        simpleNotification: SimpleNotification,
        action: Action
    ) {
        val profile = VibrationProfile.getProfile(VibrationTypeEnum.MEDIUM.name.lowercase(), 3)
        sendCustomNotification(profile, simpleNotification, action, builder)
    }

    override fun stopCurrentNotification(builder: TransactionBuilder) {
        val alert = support.getCharacteristic(GattCharacteristic.UUID_CHARACTERISTIC_ALERT_LEVEL)
        builder.write(alert, byteArrayOf(GattCharacteristic.NO_ALERT))
    }
}