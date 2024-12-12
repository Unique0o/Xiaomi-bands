package com.example.logifitappp.enums

enum class AlertCategoryEnum(val id: Int) {
    ANY(255),
    CUSTOM(-1),
    CUSTOM_HUAMI(-6),
    EMAIL(1),
    HIGH_PRIORITY_ALERT(8),
    INCOMING_CALL(3),
    INSTANT_MESSAGE(9),
    MISSED_CALL(4),
    NEWS(2),
    SCHEDULE(7),
    SIMPLE(0),
    SMS(5),
    VOICE_MAIL(6);
}