package com.example.logifitappp.core.wearebles.huami.miband

import com.example.logifitappp.core.builders.ble.TransactionBuilder
import com.example.logifitappp.core.builders.ble.actions.Action
import com.example.logifitappp.core.wearebles.SimpleNotification

interface NotificationStrategy {
    fun sendCustomNotification(vibrationProfile: VibrationProfile, simpleNotification: SimpleNotification?, flashTimes: Int, flashColour: Int, originalColour: Int, flashDuration: Long, action: Action, builder: TransactionBuilder)
    fun sendDefaultNotification(builder: TransactionBuilder, simpleNotification: SimpleNotification, action: Action)
    fun stopCurrentNotification(builder: TransactionBuilder)
}