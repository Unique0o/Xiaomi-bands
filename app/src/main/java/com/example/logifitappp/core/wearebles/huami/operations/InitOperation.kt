package com.example.logifitappp.core.wearebles.huami.operations

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import com.example.logifitappp.core.App
import com.example.logifitappp.core.builders.ble.TransactionBuilder
import com.example.logifitappp.core.builders.ble.actions.SetWearableStateAction
import com.example.logifitappp.core.utils.StringUtils
import com.example.logifitappp.core.wearebles.AbstractBleOperation
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.huami.HuamiService
import com.example.logifitappp.core.wearebles.huami.HuamiSupport
import com.example.logifitappp.enums.AppStatusCodeEnum
import org.apache.commons.lang3.ArrayUtils
import java.util.Arrays
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.math.min

open class InitOperation(
    private val support: HuamiSupport,
    private val needsAuth: Boolean,
    private val authFlags: Byte,
    private val cryptFlags: Byte,
    private val builder: TransactionBuilder
): AbstractBleOperation<HuamiSupport>(support) {
    init {
        builder.setCallback(this)
    }

    override fun doPerform() {
        support.enableNotifications(builder, true)

        if (needsAuth) {
            builder.add(SetWearableStateAction(wearable, Wearable.State.AUTHENTICATING, context))

            val sendKey = ArrayUtils.addAll(byteArrayOf(HuamiService.AUTH_SEND_KEY, authFlags), *getSecretKey())
            builder.write(getCharacteristic(HuamiService.UUID_CHARACTERISTIC_AUTH), sendKey)
        } else {
            builder.add(SetWearableStateAction(wearable, Wearable.State.INITIALIZING, context))
            builder.write(getCharacteristic(HuamiService.UUID_CHARACTERISTIC_AUTH), requestAuthNumber())
        }
    }

    protected fun getSecretKey(): ByteArray {
        val authKeyBytes = byteArrayOf(0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x40, 0x41, 0x42, 0x43, 0x44, 0x45)

        val prefs = App.getWearableSpecificSharedPrefs(wearable.getAddress())
        val authenticationKey = prefs?.getString("authentication_key", null)

        println("authentication key: $authenticationKey")

        if (!authenticationKey.isNullOrEmpty()) {
            val bytes = if (authenticationKey.length == 34 && authenticationKey.startsWith("0x")) {
                StringUtils.hexStringToByteArray(authenticationKey.substring(2))
            } else StringUtils.hexStringToByteArray(authenticationKey)

            System.arraycopy(bytes, 0, authKeyBytes, 0, min(bytes.size, 16))
        }

        return authKeyBytes
    }

    private fun handleAesAuthentication(value: ByteArray, secretKey: ByteArray): ByteArray {
        val mValue = Arrays.copyOfRange(value, 3, 19)
        @SuppressLint("GetInstance") val ecipher = Cipher.getInstance("AES/ECB/NoPadding")
        val newKey = SecretKeySpec(secretKey, "AES")
        ecipher.init(Cipher.ENCRYPT_MODE, newKey)
        return ecipher.doFinal(mValue)
    }

    override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic): Boolean {
        val uuid = characteristic.uuid

        println("characteristic response from ($uuid): ${characteristic.value.contentToString()}")

        if (HuamiService.UUID_CHARACTERISTIC_AUTH != uuid) {
            println("Unhandled characteristic changed: $uuid")
            return super.onCharacteristicChanged(gatt, characteristic)
        }

        try {
            val value = characteristic.value

            if (value[0] != HuamiService.AUTH_RESPONSE) {
                println("Got a non-response: ${value.contentToString()}")
                return super.onCharacteristicChanged(gatt, characteristic)
            }

            if (value[1] == HuamiService.AUTH_SEND_KEY && value[2] == HuamiService.AUTH_SUCCESS) {
                val builder = createTransactionBuilder("Sending the secret key to the device")
                builder.write(characteristic, requestAuthNumber())
                support.performImmediately(builder)
            } else if ((value[1].toInt() and 0x0f).toByte() == HuamiService.AUTH_REQUEST_RANDOM_AUTH_NUMBER && value[2] == HuamiService.AUTH_SUCCESS) {
                val aes = handleAesAuthentication(value, getSecretKey())
                val response = byteArrayOf((HuamiService.AUTH_SEND_ENCRYPTED_AUTH_NUMBER.toInt() or cryptFlags.toInt()).toByte(), authFlags) + aes

                val builder = createTransactionBuilder("Sending the encrypted random key to the device")
                builder.write(characteristic, response)
                support.setCurrentTimeWithService(builder)
                support.performImmediately(builder)
            } else if ((value[1].toInt() and 0x0f).toByte() == HuamiService.AUTH_SEND_ENCRYPTED_AUTH_NUMBER) {
                when (value[2]) {
                    HuamiService.AUTH_SUCCESS -> {
                        val builder = createTransactionBuilder("Authenticated, now initialize phase 2")
                        builder.add(SetWearableStateAction(wearable, Wearable.State.INITIALIZING, context))
                        builder.setCallback(null)

                        support.enableFurtherNotification(builder, true)
                        support.requestDeviceInfo(builder)
                        support.phase2Initialize(builder)
                        support.phase3Initialize(builder)
                        support.setInitialized(builder)
                        support.performImmediately(builder)
                    }

                    HuamiService.AUTH_FAIL -> {
                        println("Authentication failed, disconnecting")

                        App.signalFailedConnectionWithWearable(AppStatusCodeEnum.INVALID_WEARABLE_AUTHENTICATION_KEY)
                        App.getWearableServiceTo(wearable).disconnect()
                    }

                    else -> return super.onCharacteristicChanged(gatt, characteristic)
                }
            } else return super.onCharacteristicChanged(gatt, characteristic)
        } catch (e: Exception) {
            println("Error authenticating Huami device $e")
        }

        return true
    }

    override fun performInitialized(taskName: String): TransactionBuilder {
        throw UnsupportedOperationException("This is the initialization class, you cannot call this method")
    }

    private fun requestAuthNumber(): ByteArray {
        return if (cryptFlags == 0x00.toByte()) {
            byteArrayOf(HuamiService.AUTH_REQUEST_RANDOM_AUTH_NUMBER, authFlags)
        } else byteArrayOf((cryptFlags.toInt() or HuamiService.AUTH_REQUEST_RANDOM_AUTH_NUMBER.toInt()).toByte(), authFlags, 0x02, 0x01, 0x00)
    }
}