package com.example.logifitappp.core.wearebles.huami.miband.miband2

import com.example.logifitappp.core.builders.ble.TransactionBuilder
import com.example.logifitappp.core.builders.ble.actions.Action
import com.example.logifitappp.core.utils.GattCharacteristic
import com.example.logifitappp.core.wearebles.SimpleNotification
import com.example.logifitappp.core.wearebles.huami.HuamiSupport
import com.example.logifitappp.core.wearebles.huami.miband.V2NotificationStrategy
import com.example.logifitappp.core.wearebles.huami.miband.VibrationProfile
import com.example.logifitappp.enums.AlertCategoryEnum
import kotlin.math.max

open class Mi2NotificationStrategy(support: HuamiSupport): V2NotificationStrategy<HuamiSupport>(support) {
    private val alertLevelCharacteristic = support.getCharacteristic(GattCharacteristic.UUID_CHARACTERISTIC_ALERT_LEVEL)

    override fun sendCustomNotification(vibrationProfile: VibrationProfile, simpleNotification: SimpleNotification?, action: Action?, builder: TransactionBuilder) {
        startNotify(builder, vibrationProfile.alertLevel, simpleNotification)

        val repeat = (vibrationProfile.repeat * (vibrationProfile.onOffSequence.size / 2)).toByte()
        var waitDuration = 0

        if (repeat > 0) {
            val vibration = (vibrationProfile.onOffSequence[0]).toShort()
            val pause = (vibrationProfile.onOffSequence[1]).toShort()
            waitDuration = (vibration + pause) * repeat

            builder.write(alertLevelCharacteristic, byteArrayOf(
                -1,
                (vibration.toInt() and 255).toByte(),
                (vibration.toInt() shr 8 and 255).toByte(),
                (pause.toInt() and 255).toByte(),
                (pause.toInt() shr 8 and 255).toByte(),
                repeat
            ))
        }

        if (simpleNotification == null || simpleNotification.category != AlertCategoryEnum.INCOMING_CALL) {
            waitDuration = max(waitDuration, 4000)
            builder.wait(waitDuration)
        }

        action?.let { builder.add(it) }
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

    open protected fun startNotify(builder: TransactionBuilder, alertLevel: Int, simpleNotification: SimpleNotification?) {
        builder.write(alertLevelCharacteristic, byteArrayOf(alertLevel.toByte()))
    }

    protected fun stopNotify(builder: TransactionBuilder) {
        builder.write(alertLevelCharacteristic, byteArrayOf(GattCharacteristic.NO_ALERT))
    }
}