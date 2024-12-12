package com.example.logifitappp.core.specs

import com.example.logifitappp.enums.NotificationSpecTypeEnum
import java.io.Serializable
import java.util.concurrent.atomic.AtomicInteger

class NotificationSpec(tmpId: Int = -1) {
    var id = if (tmpId == -1) atomic.incrementAndGet() else tmpId
        private set

    var attachedActions: MutableList<Action>? = null
    var body: String? = null
    var cannedReplies: List<String>? = null
    var dndSuppressed = 0
    var flags = 0
    var key: String? = null
    var picturePath: String? = null
    var phoneNumber: String? = null
    var sender: String? = null
    var sourceAppId: String? = null
    var sourceName: String? = null
    var subject: String? = null
    var title: String? = null
    var type: NotificationSpecTypeEnum? = null
    var `when` = System.currentTimeMillis()

    companion object {
        private val atomic = AtomicInteger((System.currentTimeMillis() / 1000).toInt())
    }

    class Action: Serializable {
        var handle = 0L
        var title: String? = null
        var type = TYPE_UNDEFINED

        companion object {
            const val TYPE_SYNTECTIC_DISMISS_ALL = -1
            const val TYPE_SYNTECTIC_DISMISS = -1
            const val TYPE_SYNTECTIC_MUTE = -1
            const val TYPE_SYNTECTIC_OPEN = -1
            const val TYPE_SYNTECTIC_REPLY_PHONENR = -1
            const val TYPE_UNDEFINED = -1
            const val TYPE_WEARABLE_REPLY = -1
            const val TYPE_WEARABLE_SIMPLE = -1
        }
    }
}