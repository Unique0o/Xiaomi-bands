package com.example.logifitappp.core.wearebles.huami.zeppos.services

import androidx.annotation.RequiresPermission
import com.example.logifitappp.BuildConfig
import com.example.logifitappp.core.builders.ble.TransactionBuilder
import com.example.logifitappp.core.events.WearableCallControlEvent
import com.example.logifitappp.core.events.WearableNotificationControlEvent
import com.example.logifitappp.core.specs.CallSpec
import com.example.logifitappp.core.specs.NotificationSpec
import com.example.logifitappp.core.utils.BleTypeConversionsUtils
import com.example.logifitappp.core.utils.LimitedQueue
import com.example.logifitappp.core.utils.StringUtils
import com.example.logifitappp.core.wearebles.huami.zeppos.ZeppOsSupport
import com.example.logifitappp.enums.CallSpecTypeEnum
import com.example.logifitappp.enums.NotificationSpecTypeEnum
import org.apache.commons.lang3.ArrayUtils
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets

class ZeppOsNotificationService(support: ZeppOsSupport): AbstractZeppOsService(support, true) {
    private val notificationReplyActionQueue = LimitedQueue<Int, Long>(16)
    private val notificationPictureQueue = LimitedQueue<Int, String>(16)

    private var  supportsNotificationKey = false
    private var supportsPictures = false
    private var version = -1

    override fun getEndpoint() = ENDPOINT

    @RequiresPermission(allOf = ["android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"])
    private fun ackNotificationReply(notificationId: Int) {
        val buffer = ByteBuffer.allocate(9)
            .order(ByteOrder.LITTLE_ENDIAN)
            .put(NOTIFICATION_CMD_REPLY_ACK)
            .putInt(notificationId)
            .put(0x00)
            .put(0x00)
            .put(0x00)
            .put(0x00)

        write("ack notification reply", buffer.array())
    }

    @RequiresPermission(allOf = ["android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"])
    private fun deleteNotification(id: Int) {
        notificationPictureQueue.remove(id)
        println("Deleting notification $id from band")

        val buffer = ByteBuffer.allocate(12)
            .order(ByteOrder.LITTLE_ENDIAN)
            .put(NOTIFICATION_CMD_SEND)
            .putInt(id)
            .put(NOTIFICATION_TYPE_NORMAL)
            .put(NOTIFICATION_SUB_CMD_DISMISS_FROM_PHONE)
            .put(0x00)
            .put(0x00)
            .put(0x00)
            .put(0x00)

        write("delete notification", buffer.array())
    }

    private fun getMaxLength() = 512

    @RequiresPermission(allOf = ["android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"])
    override fun handlePayload(payload: ByteArray) {
        val buffer = ByteBuffer.wrap(payload).order(ByteOrder.LITTLE_ENDIAN)
        val cmd = buffer.get()

        val notificationControlEvent = WearableNotificationControlEvent()
        val callControlEvent = WearableCallControlEvent()

        when (cmd) {
            NOTIFICATION_CMD_CAPABILITIES_RESPONSE -> {
                version = buffer.get().toInt() and 0xff

                if (version < 4 || version > 5) println("Unsupported notification service version $version")

                if (version >= 4) {
                    buffer.getShort()
                    buffer.get()
                    buffer.get()
                    buffer.get(ByteArray(buffer.getShort().toInt()))
                }

                if (version >= 5) {
                    supportsPictures = buffer.get() != 0.toByte()
                    supportsNotificationKey = buffer.get() != 0.toByte()
                }

                println("Notification service version=$version, supportsPictures=$supportsPictures")
            }

            NOTIFICATION_CMD_REPLY -> {
                val notificationId = BleTypeConversionsUtils.toUint32(*ArrayUtils.subarray(payload, 1, 5))
                val replyHandle = notificationReplyActionQueue.lookup(notificationId)

                if (replyHandle == null) {
                    println("Failed to find reply handle for notification ID $notificationId")
                    return
                }

                val replyMessage = StringUtils.untilNullTerminator(payload, 5)

                if (replyMessage == null) {
                    println("Failed to find reply message for notification ID $notificationId")
                    return
                }

                println("Got reply to notification $notificationId with '$replyMessage'")

                notificationControlEvent.handle = replyHandle
                notificationControlEvent.event = WearableNotificationControlEvent.Event.REPLY
                notificationControlEvent.reply = replyMessage
                evaluateWearableEvent(notificationControlEvent)

                ackNotificationReply(notificationId)
                deleteNotification(notificationId)
            }

            NOTIFICATION_CMD_DISMISS -> {
                when (payload[1]) {
                    NOTIFICATION_DISMISS_NOTIFICATION -> {
                        val dismissNotificationId = BleTypeConversionsUtils.toUint32(*ArrayUtils.subarray(payload, 2, 6))
                        println("Dismiss notification $dismissNotificationId")

                        notificationControlEvent.handle = dismissNotificationId.toLong()
                        notificationControlEvent.event = WearableNotificationControlEvent.Event.DISMISS
                        evaluateWearableEvent(notificationControlEvent)
                    }

                    NOTIFICATION_DISMISS_MUTE_CALL -> {
                        println("Mute call")
                        callControlEvent.event = WearableCallControlEvent.Event.IGNORE
                        evaluateWearableEvent(callControlEvent)
                    }

                    NOTIFICATION_DISMISS_REJECT_CALL -> {
                        println("Reject call")
                        callControlEvent.event = WearableCallControlEvent.Event.REJECT
                        evaluateWearableEvent(callControlEvent)
                    }

                    else -> println("Unexpected notification dismiss byte ${payload[1]}")
                }
            }
        }
    }

    fun sendNotification(notificationSpec: NotificationSpec) {
        val baos = ByteArrayOutputStream()
        val senderOrTitle = StringUtils.getFirstOf(notificationSpec.sender, notificationSpec.title)

        try {
            baos.write(NOTIFICATION_CMD_SEND.toInt())
            baos.write(BleTypeConversionsUtils.fromUint32(notificationSpec.id))

            if (notificationSpec.type == NotificationSpecTypeEnum.GENERIC_SMS) {
                baos.write(NOTIFICATION_TYPE_SMS.toInt())
            } else baos.write(NOTIFICATION_TYPE_NORMAL.toInt())

            baos.write(NOTIFICATION_SUB_CMD_SHOW.toInt())

            if (notificationSpec.sourceAppId != null) {
                baos.write(notificationSpec.sourceAppId!!.toByteArray(StandardCharsets.UTF_8))
            } else baos.write(BuildConfig.APPLICATION_ID.toByteArray(StandardCharsets.UTF_8))

            baos.write(0x00)

            if (senderOrTitle.isNotEmpty()) baos.write(senderOrTitle.toByteArray(StandardCharsets.UTF_8))

            baos.write(0x00)

            notificationSpec.body?.let {
                baos.write(StringUtils.truncate(it, getMaxLength()).toByteArray(StandardCharsets.UTF_8))
            }

            baos.write(0x00)
            notificationSpec.sourceName?.let { baos.write(it.toByteArray(StandardCharsets.UTF_8)) }
            baos.write(0x00)

            var hasReply = false

            if (notificationSpec.attachedActions?.isNotEmpty() == true) {
                for (i in 0 until notificationSpec.attachedActions!!.size) {
                    val action = notificationSpec.attachedActions!![i]

                    when (action.type) {
                        NotificationSpec.Action.TYPE_WEARABLE_REPLY,
                        NotificationSpec.Action.TYPE_SYNTECTIC_REPLY_PHONENR -> {
                            hasReply = true
                            notificationReplyActionQueue.add(notificationSpec.id, action.handle)
                        }
                    }
                }
            }

            baos.write(if (hasReply) 1 else 0)

            if (version >= 5) baos.write(1)

            if (supportsPictures) {
                baos.write(if (notificationSpec.picturePath != null) 1 else 0)

                notificationSpec.picturePath?.let {
                    notificationPictureQueue.add(notificationSpec.id, it)
                }
            }

            if (supportsNotificationKey) {
                baos.write(notificationSpec.key!!.toByteArray(StandardCharsets.UTF_8))
                baos.write(0)
            }

            val builder = TransactionBuilder("send notification")
            write(builder, baos.toByteArray())
            support.getQueue()?.let { builder.queue(it) }
        } catch (e: Exception) {
            println("Failed to send notification $e")
        }
    }

    fun setCallState(callSpec: CallSpec) {
        val baos = ByteArrayOutputStream()

        try {
            baos.write(NOTIFICATION_CMD_SEND.toInt())
            baos.write(BleTypeConversionsUtils.fromUint32(0))
            baos.write(NOTIFICATION_TYPE_CALL.toInt())

            if (callSpec.command == CallSpecTypeEnum.CALL_INCOMING) {
                baos.write(NOTIFICATION_CALL_STATE_START.toInt())
            } else if (callSpec.command == CallSpecTypeEnum.CALL_START || callSpec.command == CallSpecTypeEnum.CALL_END) {
                baos.write(NOTIFICATION_CALL_STATE_END.toInt())
            }

            baos.write(0x00)
            callSpec.name?.let { baos.write(it.toByteArray(StandardCharsets.UTF_8)) }
            baos.write(0x00)
            baos.write(0x00)
            baos.write(0x00)
            callSpec.number?.let { baos.write(it.toByteArray(StandardCharsets.UTF_8)) }
            baos.write(0x00)
            baos.write(if (callSpec.number != null) 0x01 else 0x00)

            val builder = TransactionBuilder("send notification")
            write(builder, baos.toByteArray())
            support.getQueue()?.let { builder.queue(it) }
        } catch (e: Exception) {
            println("Failed to send call $e")
        }
    }

    companion object {
        const val NOTIFICATION_CALL_STATE_END = 0x02.toByte()
        const val NOTIFICATION_CALL_STATE_START = 0x00.toByte()
        const val NOTIFICATION_CMD_CAPABILITIES_RESPONSE = 0x02.toByte()
        const val NOTIFICATION_CMD_DISMISS = 0x05.toByte()
        const val NOTIFICATION_CMD_REPLY = 0x04.toByte()
        const val NOTIFICATION_CMD_REPLY_ACK = 0x06.toByte()
        const val NOTIFICATION_CMD_SEND = 0x03.toByte()
        const val NOTIFICATION_DISMISS_MUTE_CALL = 0x02.toByte()
        const val NOTIFICATION_DISMISS_NOTIFICATION = 0x03.toByte()
        const val NOTIFICATION_DISMISS_REJECT_CALL = 0x01.toByte()
        const val NOTIFICATION_SUB_CMD_DISMISS_FROM_PHONE = 0x02.toByte()
        const val NOTIFICATION_SUB_CMD_SHOW = 0x00.toByte()
        const val NOTIFICATION_TYPE_CALL = 0x03.toByte()
        const val NOTIFICATION_TYPE_NORMAL = 0xfa.toByte()
        const val NOTIFICATION_TYPE_SMS = 0x05.toByte()
        const val ENDPOINT = 0x001e.toShort()
    }
}