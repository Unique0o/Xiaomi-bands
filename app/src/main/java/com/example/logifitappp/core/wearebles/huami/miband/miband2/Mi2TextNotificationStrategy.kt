package com.example.logifitappp.core.wearebles.huami.miband.miband2

import com.example.logifitappp.core.builders.ble.TransactionBuilder
import com.example.logifitappp.core.builders.ble.actions.Action
import com.example.logifitappp.core.builders.ble.profiles.AlertNotificationProfile
import com.example.logifitappp.core.builders.ble.profiles.parcelables.NewAlert
import com.example.logifitappp.core.utils.BleTypeConversionsUtils
import com.example.logifitappp.core.utils.GattCharacteristic
import com.example.logifitappp.core.wearebles.SimpleNotification
import com.example.logifitappp.core.wearebles.huami.HuamiIcon
import com.example.logifitappp.core.wearebles.huami.HuamiSupport
import com.example.logifitappp.core.wearebles.huami.miband.VibrationProfile
import com.example.logifitappp.enums.AlertCategoryEnum
import com.example.logifitappp.enums.OverflowStrategyEnum

open class Mi2TextNotificationStrategy(support: HuamiSupport): Mi2NotificationStrategy(support) {
    private val newALertCharacteristic = support.getCharacteristic(GattCharacteristic.UUID_CHARACTERISTIC_NEW_ALERT)

    protected fun getNotifyMessage(simpleNotification: SimpleNotification?): ByteArray {
        val numAlerts = 1

        if (simpleNotification?.notificationType != null && simpleNotification.category != AlertCategoryEnum.SMS) {
            val customIconId = HuamiIcon.mapToIconId(simpleNotification.notificationType)

            if (customIconId == HuamiIcon.EMAIL) {
                return byteArrayOf(BleTypeConversionsUtils.fromUint8(AlertCategoryEnum.EMAIL.id), BleTypeConversionsUtils.fromUint8(numAlerts))
            }

            return byteArrayOf(BleTypeConversionsUtils.fromUint8(AlertCategoryEnum.CUSTOM_HUAMI.id), BleTypeConversionsUtils.fromUint8(numAlerts))
        }

        return byteArrayOf(BleTypeConversionsUtils.fromUint8(AlertCategoryEnum.SMS.id), BleTypeConversionsUtils.fromUint8(numAlerts))
    }

    open protected fun sendAlert(simpleNotification: SimpleNotification, builder: TransactionBuilder) {
        val profile = AlertNotificationProfile(support)
        val category = if (simpleNotification.category == AlertCategoryEnum.INCOMING_CALL) AlertCategoryEnum.INCOMING_CALL else AlertCategoryEnum.SMS

        val alert = NewAlert(category, 1, simpleNotification.message)
        profile.newAlert(builder, alert, OverflowStrategyEnum.MAKE_MULTIPLE)
    }

    override fun sendCustomNotification(vibrationProfile: VibrationProfile, simpleNotification: SimpleNotification?, action: Action?, builder: TransactionBuilder) {
        if (simpleNotification?.category == AlertCategoryEnum.INCOMING_CALL) {
            sendAlert(simpleNotification, builder)
            return
        }

        super.sendCustomNotification(vibrationProfile, simpleNotification, action, builder)

        if (simpleNotification != null && !simpleNotification.message.isNullOrEmpty()) {
            sendAlert(simpleNotification, builder)
        }
    }

    override fun startNotify(builder: TransactionBuilder, alertLevel: Int, simpleNotification: SimpleNotification?) {
        builder.write(newALertCharacteristic, getNotifyMessage(simpleNotification))
    }

    override fun stopCurrentNotification(builder: TransactionBuilder) {
        builder.write(newALertCharacteristic, byteArrayOf(AlertCategoryEnum.INCOMING_CALL.id.toByte(), 0))
    }
}