package com.example.logifitappp.core.wearebles.huami.miband.amazfitbip

import com.example.logifitappp.core.builders.ble.TransactionBuilder
import com.example.logifitappp.core.builders.ble.actions.Action
import com.example.logifitappp.core.builders.ble.profiles.AlertNotificationProfile
import com.example.logifitappp.core.builders.ble.profiles.parcelables.NewAlert
import com.example.logifitappp.core.wearebles.SimpleNotification
import com.example.logifitappp.core.wearebles.huami.HuamiSupport
import com.example.logifitappp.core.wearebles.huami.miband.VibrationProfile
import com.example.logifitappp.core.wearebles.huami.miband.miband2.Mi2TextNotificationStrategy
import com.example.logifitappp.enums.AlertCategoryEnum
import com.example.logifitappp.enums.OverflowStrategyEnum

class AmazfitBipTextNotificationStrategy(support: HuamiSupport): Mi2TextNotificationStrategy(support) {
    override fun sendCustomNotification(vibrationProfile: VibrationProfile, simpleNotification: SimpleNotification?, action: Action?, builder: TransactionBuilder) {
        simpleNotification?.let { sendAlert(it, builder) }
    }

    override fun sendAlert(simpleNotification: SimpleNotification, builder: TransactionBuilder) {
        val profile = AlertNotificationProfile(support)
        profile.maxLength = 255

        val category = when (simpleNotification.category) {
            AlertCategoryEnum.SMS, AlertCategoryEnum.EMAIL, AlertCategoryEnum.INCOMING_CALL -> simpleNotification.category
            else -> AlertCategoryEnum.SMS
        }

        val alert = NewAlert(category, 1, simpleNotification.message)
        profile.newAlert(builder, alert, OverflowStrategyEnum.TRUNCATE)
    }
}