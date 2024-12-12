package com.example.logifitappp.core.events

class WearableCallControlEvent(val event: Event = Event.UNKNOWN): AbstractWearableEvent() {
    enum class Event {
        ACCEPT,
        END,
        IGNORE,
        INCOMING,
        OUTGOING,
        REJECT,
        START,
        UNKNOWN;
    }
}