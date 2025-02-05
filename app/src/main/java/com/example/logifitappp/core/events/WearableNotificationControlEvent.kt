package com.example.logifitappp.core.events

class WearableNotificationControlEvent: AbstractWearableEvent() {
    var event = Event.UNKNOWN
    var handle = 0L
    var phoneNumber: String? = null
    var reply: String? = null
    var title: String? = null

    enum class Event {
        DISMISS,
        DISMISS_ALL,
        MUTE,
        OPEN,
        REPLY,
        UNKNOWN;
    }
}