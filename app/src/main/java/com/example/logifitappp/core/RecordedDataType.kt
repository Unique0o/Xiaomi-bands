package com.example.logifitappp.core

object RecordedDataType {
    const val TYPE_ACTIVITY = 0x00000001
    private const val TYPE_GPS_TRACKS = 0x00000004
    private const val TYPE_HEART_RATE = 0x00000080
    private const val TYPE_PAI = 0x00000100
    private const val TYPE_SLEEP_RESPIRATORY_RATE = 0x00000200
    private const val TYPE_SPO2 = 0x00000020
    private const val TYPE_STRESS = 0x00000040
    const val TYPE_SYNC = TYPE_ACTIVITY or TYPE_GPS_TRACKS or TYPE_SPO2 or TYPE_STRESS or TYPE_HEART_RATE or TYPE_PAI or TYPE_SLEEP_RESPIRATORY_RATE
}