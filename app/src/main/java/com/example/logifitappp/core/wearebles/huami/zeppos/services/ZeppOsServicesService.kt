package com.example.logifitappp.core.wearebles.huami.zeppos.services

import com.example.logifitappp.core.builders.ble.TransactionBuilder
import com.example.logifitappp.core.wearebles.huami.zeppos.ZeppOsSupport
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ZeppOsServicesService(private val support: ZeppOsSupport): AbstractZeppOsService(support, false) {
    override fun getEndpoint() = ENDPOINT

    override fun handlePayload(payload: ByteArray) {
        when (payload[0]) {
            CMD_RET_LIST -> handleSupportedServices(payload)
            else -> println("Unexpected services payload byte ${payload[0]}")
        }
    }

    private fun handleSupportedServices(payload: ByteArray) {
        val buffer = ByteBuffer.wrap(payload).order(ByteOrder.LITTLE_ENDIAN)
        buffer.get()

        val numServices = buffer.getShort()

        for (i in 0 ..< numServices) {
            val endpoint = buffer.getShort()
            val encryptedByte = buffer.get()

            val encrypted = booleanFromByte(encryptedByte)
            val service = support.getService(endpoint)

            if (service != null && encrypted != null) service.setEncrypted(encrypted)

            support.addSupportedService(endpoint, encrypted != null && encrypted)
        }

        support.initializeServices()

        val remainingBytes = buffer.limit() - buffer.position()

        if (remainingBytes != 0) println("There are $remainingBytes bytes remaining in the buffer")
    }

    fun requestServices(builder: TransactionBuilder) {
        write(builder, CMD_GET_LIST)
    }

    companion object {
        const val CMD_GET_LIST = 0x03.toByte()
        const val CMD_RET_LIST = 0x04.toByte()
        const val ENDPOINT = 0x0000.toShort()
    }
}