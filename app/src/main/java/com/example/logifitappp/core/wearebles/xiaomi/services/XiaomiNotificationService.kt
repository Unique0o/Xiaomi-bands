package com.example.logifitappp.core.wearebles.xiaomi.services

import android.Manifest
import android.content.pm.PackageManager
import com.example.logifitappp.core.events.WearableNotificationControlEvent
import com.example.logifitappp.core.specs.CallSpec
import com.example.logifitappp.core.wearebles.xiaomi.XiaomiSupport
import com.example.logifitappp.enums.CallSpecTypeEnum
import nodomain.freeyourgadget.gadgetbridge.proto.xiaomi.XiaomiProto
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class XiaomiNotificationService(support: XiaomiSupport): AbstractXiaomiService(support) {
    private fun canSendSms(): Boolean {
        return support.getContext().checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
    }

    override fun handleCommand(cmd: XiaomiProto.Command) {
        val notificationControlEvent = WearableNotificationControlEvent()

        when (cmd.subtype) {
            CMD_NOTIFICATION_DISMISS -> {
                println("Watch dismiss ${cmd.notification.notificationDismiss.notificationIdCount} notifications")

                for (notificationId in cmd.notification.notificationDismiss.notificationIdList) {
                    println("Watch dismiss ${notificationId.id}")
                    notificationControlEvent.handle = notificationId.id.toLong()
                    notificationControlEvent.event = WearableNotificationControlEvent.Event.DISMISS
                    support.evaluateWearableEvent(notificationControlEvent)
                }
            }
        }
    }

    fun onSetCallState(callSpec: CallSpec) {
        if (callSpec.command == CallSpecTypeEnum.CALL_OUTGOING) return

        if (callSpec.command != CallSpecTypeEnum.CALL_INCOMING) {
            val notification4 = XiaomiProto.NotificationDismiss.newBuilder()
                .addNotificationId(
                    XiaomiProto.NotificationId.newBuilder()
                        .setId(0)
                        .setPackage("phone")
                )

            val notification = XiaomiProto.Notification.newBuilder()
                .setNotificationDismiss(notification4)
                .build()

            support.sendCommand(
                "send call end",
                XiaomiProto.Command.newBuilder()
                    .setType(COMMAND_TYPE)
                    .setSubtype(CMD_NOTIFICATION_DISMISS)
                    .setNotification(notification)
                    .build()
            )

            return
        }

        val notification3 = XiaomiProto.Notification3.newBuilder()
            .setId(0)
            .setUnknown4("")
            .setIsCall(true)
            .setRepliesAllowed(canSendSms())
            .setTimestamp(TIMESTAMP_SDF.format(Date()))

        notification3.setPackage("phone")
        notification3.setAppName("phone")
        notification3.setTitle(callSpec.name ?: "?")
        notification3.setBody(callSpec.number ?: "?")

        val notification2 = XiaomiProto.Notification2.newBuilder()
            .setNotification3(notification3)
            .build()

        val notification = XiaomiProto.Notification.newBuilder()
            .setNotification2(notification2)
            .build()

        support.sendCommand(
            "send call",
            XiaomiProto.Command.newBuilder()
                .setType(COMMAND_TYPE)
                .setSubtype(CMD_NOTIFICATION_SEND)
                .setNotification(notification)
                .build()
        )
    }

    companion object {
        const val CMD_NOTIFICATION_DISMISS = 1
        const val CMD_NOTIFICATION_SEND = 0
        const val COMMAND_TYPE = 7

        val TIMESTAMP_SDF = SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.ROOT)
    }
}