package com.example.logifitappp.core.wearebles.huami.operations

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import com.example.logifitappp.core.App
import com.example.logifitappp.core.builders.ble.TransactionBuilder
import com.example.logifitappp.core.builders.ble.actions.SetWearableStateAction
import com.example.logifitappp.core.utils.CryptoUtils
import com.example.logifitappp.core.utils.ECDH_B163
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.huami.Huami2021ChunkedDecoder
import com.example.logifitappp.core.wearebles.huami.Huami2021ChunkedEncoder
import com.example.logifitappp.core.wearebles.huami.Huami2021Handler
import com.example.logifitappp.core.wearebles.huami.Huami2021Service
import com.example.logifitappp.core.wearebles.huami.HuamiService
import com.example.logifitappp.core.wearebles.huami.HuamiSupport
import java.util.Random

class InitOperation2021(
    private val support: HuamiSupport,
    needsAuth: Boolean,
    authFlags: Byte,
    cryptFlags: Byte,
    private val builder: TransactionBuilder,
    private val huami2021ChunkedEncoder: Huami2021ChunkedEncoder?,
    private val huami2021ChunkedDecoder: Huami2021ChunkedDecoder?
): InitOperation(support, needsAuth, authFlags, cryptFlags, builder), Huami2021Handler {
    private lateinit var publicEc: ByteArray
    private lateinit var sharedEc: ByteArray

    private val finalSharedSessionAes = ByteArray(16)
    private val privateEc = ByteArray(24)
    private val remotePublic = ByteArray(48)
    private val remoteRandom = ByteArray(16)

    init {
        huami2021ChunkedDecoder?.setHuami2021Handler(this)
    }

    override fun doPerform() {
        support.enableNotifications(builder, true)
        builder.add(SetWearableStateAction(wearable, Wearable.State.INITIALIZING, context))
        generateKeyPair()

        val sendPubKeyCommand = ByteArray(48 + 4)
        sendPubKeyCommand[0] = 0x04
        sendPubKeyCommand[1] = 0x02
        sendPubKeyCommand[2] = 0x00
        sendPubKeyCommand[3] = 0x02
        System.arraycopy(publicEc, 0, sendPubKeyCommand, 4, 48)

        huami2021ChunkedEncoder?.write(builder, Huami2021Service.CHUNKED2021_ENDPOINT_AUTH, sendPubKeyCommand, true, false)
    }

    private fun generateKeyPair() {
        val random = Random()
        random.nextBytes(privateEc)
        publicEc = ECDH_B163.ecdh_generate_public(privateEc) ?: byteArrayOf()
    }

    override fun handle2021Payload(type: Short, payload: ByteArray) {
        if (type != Huami2021Service.CHUNKED2021_ENDPOINT_AUTH) {
            support.handle2021Payload(type, payload)
            return
        }

        if (payload[0] == HuamiService.RESPONSE && payload[1] == 0x04.toByte() && payload[2] == HuamiService.SUCCESS) {
            println("Got remote random + public key")

            System.arraycopy(payload, 3, remoteRandom, 0, 16)
            System.arraycopy(payload, 19, remotePublic, 0, 48)
            sharedEc = ECDH_B163.ecdh_generate_shared(privateEc, remotePublic) ?: byteArrayOf()

            val encryptedSequenceNumber = (sharedEc[0].toInt() and 0xff) or ((sharedEc[1].toInt() and 0xff) shl 8) or ((sharedEc[2].toInt() and 0xff) shl 16) or ((sharedEc[3].toInt() and 0xff) shl 24)
            val secretKey = getSecretKey()

            for (i in 0 ..< 16) finalSharedSessionAes[i] = (sharedEc[i + 8].toInt() xor secretKey[i].toInt()).toByte()

            println("Shared Session Key: ${finalSharedSessionAes.contentToString()}")
            huami2021ChunkedEncoder?.setEncryptionParameters(encryptedSequenceNumber, finalSharedSessionAes)
            huami2021ChunkedDecoder?.setEncryptionParameters(finalSharedSessionAes)

            try {
                val encryptedRandom1 = CryptoUtils.encryptAES(remoteRandom, secretKey)
                val encryptedRandom2 = CryptoUtils.encryptAES(remoteRandom, finalSharedSessionAes)

                if (encryptedRandom1.size == 16 && encryptedRandom2.size == 16) {
                    val command = ByteArray(33)
                    command[0] = 0x05
                    System.arraycopy(encryptedRandom1, 0, command, 1, 16)
                    System.arraycopy(encryptedRandom2, 0, command, 17, 16)

                    val builder = createTransactionBuilder("Sending double encryted random to device")
                    huami2021ChunkedEncoder?.write(builder, Huami2021Service.CHUNKED2021_ENDPOINT_AUTH, command, true, false)
                    support.performImmediately(builder)
                }
            } catch (e: Exception) {
                println("AES encryption failed $e")
            }
        } else if (payload[0] == HuamiService.RESPONSE && payload[1] == 0x05.toByte() && payload[2] == HuamiService.SUCCESS) {
            println("Auth Success")

            try {
                val builder = createTransactionBuilder("Authenticated, now initialize phase 2")
                builder.add(SetWearableStateAction(wearable, Wearable.State.INITIALIZING, context))
                builder.setCallback(this)

                support.enableFurtherNotification(builder, true)
                support.setCurrentTimeWithService(builder)
                support.requestDeviceInfo(builder)
                support.phase2Initialize(builder)
                support.phase3Initialize(builder)
                support.setInitialized(builder)
                support.performImmediately(builder)
            } catch (e: Exception) {
                println("failed initializing device $e")
            }
        } else if (payload[0] == HuamiService.RESPONSE && payload[1] == 0x05.toByte() && payload[2] == 0x25.toByte()) {
            println("Authentication failed, disconnecting")

            App.signalAuthenticationKeyFailed()
            App.getWearableServiceTo(wearable).disconnect()
        } else {
            println("Unhandled auth payload: ${payload.contentToString()}")
        }
    }

    override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic): Boolean {
        val uuid = characteristic.uuid

        if (uuid != HuamiService.UUID_CHARACTERISTIC_CHUNKEDTRANSFER_2021_READ) {
            println("Unhandled characteristic changed: $uuid")
            return super.onCharacteristicChanged(gatt, characteristic)
        }

        val value = characteristic.value

        if (value.size <= 1 || value[0] != 0x03.toByte()) return super.onCharacteristicChanged(gatt, characteristic)

        if (huami2021ChunkedDecoder?.decode(value) == true) support.sendChunkedAck()

        return true
    }

    override fun performInitialized(taskName: String): TransactionBuilder {
        throw UnsupportedOperationException("This IS the initialization class, you cannot call this method")
    }
}