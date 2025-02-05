package com.example.logifitappp.core.wearebles.fitpro

import java.util.UUID

object FitProConstants {
    const val CMD_GROUP_BAND_INFO = 0x20.toByte()
    const val CMD_GROUP_GENERAL = 0x12.toByte()
    const val CMD_GROUP_RECEIVE_SPORTS_DATA = 0x15.toByte()
    const val CMD_GROUP_REQUEST_DATA = 0x1a.toByte()

    const val CMD_GET_HW_INFO = 0x10.toByte()
    const val CMD_INIT1 = 0xa.toByte()
    const val CMD_INIT2 = 0xc.toByte()
    const val CMD_INIT3 = 0xff.toByte()
    const val CMD_REQUEST_STEPS_DATA0x7 = 0x7.toByte()
    const val CMD_REQUEST_STEPS_DATA0x8 = 0x8.toByte()
    const val CMD_REQUEST_STEPS_DATA1 = 0x6.toByte()
    const val CMD_REQUEST_STEPS_DATA0x10 = 0x10.toByte()
    const val CMD_RX_BAND_INFO = 0x2.toByte()
    const val CMD_SET_DATE_TIME = 0x1.toByte()

    const val DATA_HEADER = 0xcd.toByte()
    const val DATA_HEADER_ACK = 0xdc.toByte()
    val DATA_TEMPLATE = byteArrayOf(DATA_HEADER, 0x00, 0, 0, 0x1, 0, 0x0, 0)

    val UUID_CHARACTERISTIC_RX: UUID = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9d")
    val UUID_CHARACTERISTIC_TX: UUID = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9d")
    val UUID_CHARACTERISTIC_UART: UUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9d")

    const val RX_SLEEP_DATA = 0x3.toByte()
    const val RX_SPORTS_DAY_DATA = 0xc.toByte()
    const val RX_STEP_DATA = 0x2.toByte()

    const val VALUE_ON = 0x1.toByte()
    const val VALUE_OFF = 0x0.toByte()
}