package com.example.logifitappp.core.wearebles.xiaomi.services

import android.content.Context
import android.media.MediaCodec.CryptoException
import android.os.Build
import com.example.logifitappp.core.App
import com.example.logifitappp.core.utils.StringUtils
import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.core.wearebles.WearableUpdateSubjectEnum
import com.example.logifitappp.core.wearebles.xiaomi.XiaomiSupport
import com.google.protobuf.ByteString
import nodomain.freeyourgadget.gadgetbridge.proto.xiaomi.XiaomiProto
import org.bouncycastle.crypto.engines.AESEngine
import org.bouncycastle.crypto.modes.CCMBlockCipher
import org.bouncycastle.crypto.params.AEADParameters
import org.bouncycastle.crypto.params.KeyParameter
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.SecureRandom
import java.util.Locale
import javax.crypto.Mac
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class XiaomiAuthService(support: XiaomiSupport) : AbstractXiaomiService(support) {
    private val decryptionKey = ByteArray(16)
    private var checkDecryptionMac = true
    private val decryptionNonce = ByteArray(4)
    private var encryptionInitialized = false
    private val encryptionKey = ByteArray(16)
    private val encryptionNonce = ByteArray(4)
    private val nonce = ByteArray(16)
    private val secretKey = ByteArray(16)

    private fun buildNonceCommand(nonce: ByteArray): XiaomiProto.Command {
        val phoneNonce = XiaomiProto.PhoneNonce.newBuilder()
        phoneNonce.setNonce(ByteString.copyFrom(nonce))

        val auth = XiaomiProto.Auth.newBuilder()
        auth.setPhoneNonce(phoneNonce)

        val command = XiaomiProto.Command.newBuilder()
        command.setType(COMMAND_TYPE)
        command.setSubtype(CMD_NONCE)
        command.setAuth(auth.build())

        return command.build()
    }

    private fun computeAuthStep3Hmac(secretKey: ByteArray, phoneNonce: ByteArray, watchNonce: ByteArray): ByteArray {
        val miwearAuthBytes = "miwear-auth".toByteArray()
        val mac: Mac = try {
            Mac.getInstance("HmacSHA256"). apply {
                init(SecretKeySpec(phoneNonce + watchNonce, "HmacSHA256"))
                init(SecretKeySpec(doFinal(secretKey), "HmacSHA256"))
            }
        } catch (e: Exception) {
            throw IllegalStateException("Failed to initialize hmac for auth step 2", e)
        }

        val output = ByteArray(64)
        var tmp = ByteArray(0)
        var b: Byte = 1
        var i = 0

        while (i < output.size) {
            mac.update(tmp)
            mac.update(miwearAuthBytes)
            mac.update(b) //b.toInt().toByte()
            tmp = mac.doFinal()

            for (j in tmp.indices) {
                if (i < output.size) {
                    output[i] = tmp[j]
                    i++

                    continue
                }

                break
            }

            b++
        }

        return output
    }

    private fun createBlockCipher(forEncryption: Boolean, secretKey: SecretKey, macSizeBits: Int, nonce: ByteArray): CCMBlockCipher {
        val aesFastEngine = AESEngine()
        aesFastEngine.init(forEncryption, KeyParameter(secretKey.encoded))

        return CCMBlockCipher(aesFastEngine).apply {
            init(forEncryption, AEADParameters(KeyParameter(secretKey.encoded), macSizeBits, nonce, null))
        }
    }

    fun decrypt(payload: ByteArray): ByteArray {
        val packetNonce = ByteBuffer.allocate(12).order(ByteOrder.LITTLE_ENDIAN)
        packetNonce.put(decryptionNonce)
        packetNonce.putInt(0)
        packetNonce.putInt(0)

        try {
            return decrypt(decryptionKey, packetNonce.array(), payload, checkDecryptionMac)
        } catch (e: CryptoException) {
            throw RuntimeException("failed to decrypt", e)
        }
    }

    private fun decrypt(key: ByteArray, nonce: ByteArray, payload: ByteArray, checkMac: Boolean): ByteArray {
        val macSizeBits = if (checkMac) 32 else 0
        val encryptedLength = if (checkMac) payload.size else payload.size - 4

        val cipher = createBlockCipher(false, SecretKeySpec(key, "AES"), macSizeBits, nonce)
        val out = ByteArray(cipher.getOutputSize(encryptedLength))

        val bytes = cipher.processBytes(payload, 0, encryptedLength, out, 0)
        cipher.doFinal(out, bytes)

        return out
    }

    fun encrypt(payload: ByteArray, i: Int): ByteArray {
        val packetNonce = ByteBuffer.allocate(12).order(ByteOrder.LITTLE_ENDIAN)
            .put(encryptionNonce)
            .putInt(0)
            .putInt(i)

        try {
            return encrypt(encryptionKey, packetNonce.array(), payload)
        } catch (e: CryptoException) {
            throw RuntimeException("failed to encrypt", e)
        }
    }

    private fun encrypt(key: ByteArray, nonce: ByteArray, payload: ByteArray): ByteArray {
        val cipher = createBlockCipher(true, SecretKeySpec(key, "AES"), 32, nonce)
        val out = ByteArray(cipher.getOutputSize(payload.size))

        val bytes = cipher.processBytes(payload, 0, payload.size, out, 0)
        cipher.doFinal(out, bytes)

        return out
    }

    private fun getSecretKey(wearable: Wearable): ByteArray {
        val authenticationKeyBytes = ByteArray(16)
        val preferences = App.getWearableSpecificSharedPrefs(wearable.getAddress())!!
        val authenticationKey = preferences.getString("authentication_key", "")!!.trim()

        if (authenticationKey.isNotBlank()) {
            val bytes = if (authenticationKey.length == 34 && authenticationKey.startsWith("0x")) {
                StringUtils.hexStringToByteArray(authenticationKey.substring(2))
            } else StringUtils.hexStringToByteArray(authenticationKey)

            System.arraycopy(bytes, 0, authenticationKeyBytes, 0, bytes.size.coerceAtMost(16))
        }

        return authenticationKeyBytes
    }

    private fun getUserId(wearable: Wearable): String {
        val preferences = App.getWearableSpecificSharedPrefs(wearable.getAddress())
        val authenticationKey = preferences!!.getString("authentication_key", null)

        if (authenticationKey.isNullOrBlank()) return "0000000000"

        return authenticationKey
    }

    override fun handleCommand(cmd: XiaomiProto.Command) {
        if (cmd.type != COMMAND_TYPE) throw IllegalArgumentException("Not an auth command")

        when (cmd.subtype) {
            CMD_NONCE -> {
                println("Got watch nonce")

                val command = handleWatchNonce(cmd.auth.watchNonce)

                if (command == null) {
                    // TODO AUTHENTICATION FAILED

                    println("handleWatchNonce returned null, disconnecting")
                    App.getWearableServiceTo(support.getWearable()).disconnect()
                    return
                }

                support.sendCommand("auth step 2", command)
            }

            CMD_AUTH,
            CMD_SEND_USERID -> {
                if (cmd.subtype == CMD_AUTH || cmd.auth.status == 1) {
                    encryptionInitialized = cmd.subtype == CMD_AUTH

                    println("Authenticated, further communications are ${if (encryptionInitialized) "encrypted" else "in plaintext"}")

                    support.getWearable().apply {
                        setState(Wearable.State.INITIALIZED)
                        sendDeviceUpdateIntent(support.getContext(), WearableUpdateSubjectEnum.DEVICE_STATE)
                    }

                    support.onAuthSuccess()
                } else {
                    // TODO AUTHENTICATION FAILED

                    println("Authentication failed, subtype=${cmd.subtype}, status=${cmd.status}")
                    App.getWearableServiceTo(support.getWearable()).disconnect()
                }
            }

            else -> println("Unknown auth payload subtype ${cmd.subtype}")
        }
    }

    private fun handleWatchNonce(watchNonce: XiaomiProto.WatchNonce): XiaomiProto.Command? {
        val step2hmac = computeAuthStep3Hmac(secretKey, nonce, watchNonce.nonce.toByteArray())

        System.arraycopy(step2hmac, 0, decryptionKey, 0, 16)
        System.arraycopy(step2hmac, 16, encryptionKey, 0, 16)
        System.arraycopy(step2hmac, 32, decryptionNonce, 0, 4)
        System.arraycopy(step2hmac, 36, encryptionNonce, 0, 4)

        println("decryptionKey: ${decryptionKey.contentToString()}")
        println("encryptionKey: ${encryptionKey.contentToString()}")
        println("decryptionNonce: ${decryptionNonce.contentToString()}")
        println("encryptionNonce: ${encryptionNonce.contentToString()}")

        val decryptionConfirmation = hmacSHA256(decryptionKey, watchNonce.nonce.toByteArray() + nonce)

        if (!decryptionConfirmation.contentEquals(watchNonce.hmac.toByteArray())) {
            println("Watch hmac mismatch")
            return null
        }

        val authDeviceInfo = XiaomiProto.AuthDeviceInfo.newBuilder()
            .setUnknown1(0)
            .setPhoneApiLevel(Build.VERSION.SDK_INT.toFloat())
            .setPhoneName(Build.MODEL)
            .setUnknown3(224)
            .setRegion(Locale.getDefault().language.substring(0, 2).uppercase(Locale.ROOT))
            .build()

        val encryptedNonces = hmacSHA256(encryptionKey, nonce + watchNonce.nonce.toByteArray())
        val encryptedDeviceInfo = encrypt(authDeviceInfo.toByteArray(), 0)
        val authStep3 = XiaomiProto.AuthStep3.newBuilder()
            .setEncryptedNonces(ByteString.copyFrom(encryptedNonces))
            .setEncryptedDeviceInfo(ByteString.copyFrom(encryptedDeviceInfo))
            .build()

        val cmd = XiaomiProto.Command.newBuilder()
        cmd.type = COMMAND_TYPE
        cmd.subtype = CMD_AUTH

        val auth = XiaomiProto.Auth.newBuilder()
        auth.authStep3 = authStep3

        return cmd.setAuth(auth.build()).build()
    }

    private fun hmacSHA256(key: ByteArray, input: ByteArray): ByteArray {
        try {
            val mac = Mac.getInstance("HmacSHA256").apply {
                init(SecretKeySpec(key, "HmacSHA256"))
            }

            return mac.doFinal(input)
        } catch (e: Exception) {
            throw RuntimeException("Filed to hmac", e)
        }
    }

    fun isEncryptionInitialized() = encryptionInitialized

    override fun setContext(context: Context) {
        super.setContext(context)
        this.checkDecryptionMac = coordinator.checkDecryptionMac()
    }

    fun startClearTextHandshake() {
        val auth = XiaomiProto.Auth.newBuilder()
            .setUserId(getUserId(support.getWearable()))
            .build()

        val command = XiaomiProto.Command.newBuilder()
            .setType(COMMAND_TYPE)
            .setSubtype(CMD_SEND_USERID)
            .setAuth(auth)
            .build()

        support.sendCommand("auth step 1", command)
    }

    fun startEncryptedHandshake() {
        encryptionInitialized = false

        System.arraycopy(getSecretKey(support.getWearable()), 0, secretKey, 0, 16)
        SecureRandom().nextBytes(nonce)

        support.sendCommand("auth step 1", buildNonceCommand(nonce))
    }

    companion object {
        private const val CMD_AUTH = 27
        private const val CMD_NONCE = 26
        private const val CMD_SEND_USERID = 5
        const val COMMAND_TYPE = 1
    }
}