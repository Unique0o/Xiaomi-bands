package com.example.logifitappp.core.wearebles.huami.zeppos.services

import com.example.logifitappp.core.events.WearableCallControlEvent
import com.example.logifitappp.core.events.WearableNotificationControlEvent
import com.example.logifitappp.core.wearebles.huami.zeppos.ZeppOsSupport
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ZeppOsNotificationService(private val support: ZeppOsSupport): AbstractZeppOsService(support, true) {
    override fun getEndpoint() = ENDPOINT

    override fun handlePayload(payload: ByteArray) {
        val buffer = ByteBuffer.wrap(payload).order(ByteOrder.LITTLE_ENDIAN)
        val cmd = buffer.get()

        val notificationControlEvent = WearableNotificationControlEvent()
        val callControlEvent = WearableCallControlEvent()
    }

    companion object {
        const val NOTIFICATION_CMD_CAPABILITIES_RESPONSE = 0x02.toByte()
        const val ENDPOINT = 0x001e.toShort()
    }
}