package com.example.logifitappp.enums

enum class ActivityTypeEnum(val value: Int) {
    TYPE_ACTIVITY(1),
    TYPE_CYCLING(128),
    TYPE_DEEP_SLEEP(4),
    TYPE_LIGHT_SLEEP(2),
    TYPE_NOT_WORN(8),
    TYPE_REM_SLEEP(0x01000000),
    TYPE_RUNNING(0x00000010),
    TYPE_UNKNOWN(0);
}
