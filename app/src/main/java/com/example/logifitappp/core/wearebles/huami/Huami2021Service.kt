package com.example.logifitappp.core.wearebles.huami

object Huami2021Service {
    const val BATTERY_REQUEST = 0x03.toByte()
    const val BATTERY_REPLY = 0x04.toByte()

    const val CONNECTION_CMD_MTU_REQUEST = 0x01.toByte()
    const val CONNECTION_CMD_MTU_RESPONSE = 0x02.toByte()
    const val CONNECTION_CMD_UNKNOWN_3 = 0x03.toByte()
    const val CONNECTION_CMD_UNKNOWN_4 = 0x04.toByte()

    const val CHUNKED2021_ENDPOINT_AUTH = 0x0082.toShort()
    const val CHUNKED2021_ENDPOINT_BATTERY = 0x0029.toShort()
    const val CHUNKED2021_ENDPOINT_COMPAT = 0x0090.toShort()
    const val CHUNKED2021_ENDPOINT_CONNECTION = 0x0015.toShort()
}