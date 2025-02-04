package com.example.logifitappp.core.wearebles.huami.zeppos.services

import com.example.logifitappp.core.Preferences
import com.example.logifitappp.core.builders.ble.TransactionBuilder
import com.example.logifitappp.core.events.WearableUpdatePreferencesEvent
import com.example.logifitappp.core.utils.StringUtils
import com.example.logifitappp.core.wearebles.WearablePreferenceConst
import com.example.logifitappp.core.wearebles.huami.zeppos.ZeppOsSupport
import org.apache.commons.lang3.ArrayUtils
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.Collections

class ZeppOsConfigService(support: ZeppOsSupport): AbstractZeppOsService(support, true) {
    private val groupVersions = mutableMapOf<ConfigGroupEnum, Byte?>()

    override fun getEndpoint() = ENDPOINT

    private fun handle2021ConfigResponse(payload: ByteArray) {
        val configGroup = ConfigGroupEnum.fromValue(payload[2])

        if (configGroup == null) {
            println("Unknown config type ${String.format("0x%02x", payload[2])}")
            return
        }

        val version = payload[3]

        if (configGroup.version != version) {
            if (!(configGroup == ConfigGroupEnum.HEALTH && version == 1.toByte())) {
                println("Unexpected version ${String.format("0x%02x", version)} for $configGroup")
                return
            }
        }

        groupVersions[configGroup] = version

        val includesConstraints = payload[4] == 0x01.toByte()
        val numConfigs = payload[5].toInt() and 0xff

        println("Got $numConfigs configs for $configGroup version $version")

        ConfigParser(configGroup, includesConstraints)
            .parse(numConfigs, ArrayUtils.subarray(payload, 6, payload.size))
            ?.let { prefs ->
                val updatePreferenceEvent = WearableUpdatePreferencesEvent(prefs)
                support.evaluateWearableEvent(updatePreferenceEvent)

                if (support.getWearable().isInitialized()) {
                    //TODO: when require device settings
                }
            }
    }

    private fun handleCapabilitiesResponse(payload: ByteArray) {
        val version = payload[1].toInt() and 0xff
        println("Got config service version= $version")

        if (version > 3) {
            println("Unsupported config service version $version")
            return
        }

        val numGroups = payload[2].toInt() and 0xff

        if (payload.size != numGroups + 3) {
            println("Unexpected config capabilities response length ${payload.size} for $numGroups groups")
            return
        }

        for (i in 0 until numGroups) {
            val configGroup = ConfigGroupEnum.fromValue(payload[3 + i])
            println("Got supported config group ${String.format("0x%02x", payload[3 + i])}: $configGroup")
        }
    }

    override fun handlePayload(payload: ByteArray) {
        when (payload[0]) {
            CMD_CAPABILITIES_RESPONSE ->  handleCapabilitiesResponse(payload)
            CMD_ACK -> println("Configuration ACK, status = ${payload[1]}")

            CMD_RESPONSE -> {
                if (payload[1] != 1.toByte()) {
                    println("Configuration response not success: ${payload[1]}")
                    return
                }

                handle2021ConfigResponse(payload)
            }

            else -> println("Unexpected configuration payload byte ${String.format("0x%02x", payload[0])}")
        }
    }

    override fun initialize(builder: TransactionBuilder) {
        val prefs = support.getWearablePrefs()

        for (arg in ConfigArgEnum.entries) {
            if (arg.prefKey == null) continue

            prefs.getPreferences()
                .edit()
                .putBoolean(arg.prefKey, true)
                .apply()

            onSendConfiguration(arg.prefKey, prefs)
        }
    }

    override fun onSendConfiguration(config: String, prefs: Preferences): Boolean {
        if (!PREF_TO_CONFIG.containsKey(config)) return false

        val configSetter = ConfigSetter()

        if (setConfig(prefs, config, configSetter)) {
            try {
                val builder = TransactionBuilder("send config $config")
                configSetter.write(builder)
                support.getQueue()?.let { builder.queue(it) }
            } catch (e: Exception) {
                println("Error setting configuration")
            }

            return true
        }

        return false
    }

    private fun setConfig(prefs: Preferences, key: String, setter: ConfigSetter): Boolean {
        val configArg = PREF_TO_CONFIG[key] ?: return false.also { println("Unknown pref key: $key") }

        when (configArg.getConfigType(groupVersions)) {
            ConfigTypeEnum.BOOL -> {
                setter.setBoolean(configArg, prefs.getBoolean(key, false))
                return true
            }

            else -> return false.also { println("Filed to set $configArg") }
        }
    }

    enum class ConfigArgEnum(val configGroup: ConfigGroupEnum, val configType: ConfigTypeEnum, val code: Byte, val prefKey: String?) {
        HEART_RATE_ACTIVITY_MONITORING(ConfigGroupEnum.HEALTH, ConfigTypeEnum.BOOL, 0x04, WearablePreferenceConst.PREF_HEART_RATE_ACTIVITY_MONITORING),
        SLEEP_HIGH_ACCURACY_MONITORING(ConfigGroupEnum.HEALTH, ConfigTypeEnum.BOOL, 0x11, WearablePreferenceConst.PREF_HEART_RATE_USE_FOR_SLEEP_DETECTION),
        SPO2_ALL_DAY_MONITORING(ConfigGroupEnum.HEALTH, ConfigTypeEnum.BOOL, 0x31, WearablePreferenceConst.PREF_SPO2_ALL_DAY_MONITORING),
        STRESS_MONITORING(ConfigGroupEnum.HEALTH, ConfigTypeEnum.BOOL, 0x13, WearablePreferenceConst.PREF_HEART_RATE_STRESS_MONITORING);

        fun getConfigType(groupVersions: Map<ConfigGroupEnum, Byte?>): ConfigTypeEnum {
            return configType
        }

        companion object {
            fun fromCode(configGroup: ConfigGroupEnum, code: Byte): ConfigArgEnum? {
                entries.forEach {
                    if (it.configGroup == configGroup && it.code == code) return it
                }

                return null
            }
        }
    }

    enum class ConfigGroupEnum(val value: Byte, val version: Byte) {
        AGPS(0x00, 0x01),
        BLUETOOTH(0x0b, 0x01),
        DISPLAY(0x01, 0x02),
        HEALTH(0x08, 0x02),
        LANGUAGE(0x07, 0x02),
        LOCKSCREEN(0x04, 0x01),
        OFFLINE_VOICE(0x06, 0x02),
        SOUND_AND_VIBRATION(0x03, 0x02),
        SYSTEM(0x0a, 0x01),
        WEARING_DIRECTION(0x05, 0x02),
        WORKOUT(0x09, 0x01);

        companion object {
            fun fromValue(value: Byte): ConfigGroupEnum? {
                entries.forEach {
                    if (it.value == value) return it
                }

                return null
            }
        }
    }

    enum class ConfigTypeEnum(val value: Byte) {
        BOOL(0x0b),
        BYTE(0x10),
        BYTE_LIST(0x11),
        DATETIME_HH_MM(0x30),
        INT(0x03),
        SHORT(0x01),
        STRING(0x20),
        STRING_LIST(0x21),
        TIMESTAMP_MILLIS(0x40);

        companion object {
            fun fromValue(value: Byte): ConfigTypeEnum? {
                entries.forEach {
                    if (it.value == value) return it
                }

                return null
            }
        }
    }

    private class ConfigBoolean(val value: Boolean) {
        companion object {
            fun consume(buffer: ByteBuffer) = ConfigBoolean(buffer.get() == 1.toByte())
        }
    }

    private inner class ConfigParser(private val configGroup: ConfigGroupEnum, private val includesConstraints: Boolean) {
        private fun convertBooleanToPrefs(configArg: ConfigArgEnum, value: ConfigBoolean): Map<String, Any?>? {
            if (configArg.prefKey != null)  return singletonMap(configArg.prefKey, value.value)

            return null
        }

        private fun convertStringToPrefs(configArg: ConfigArgEnum, value: ConfigString): Map<String, Any?>? {
            if (configArg.prefKey != null) return singletonMap(configArg.prefKey, value.value)

            return null
        }

        fun parse(expectedNumConfigs: Int, bytes: ByteArray): Map<String, Any?>? {
            val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)
            val prefs = mutableMapOf<String, Any?>()

            var configCount = 0

            while (buffer.position() < buffer.limit()) {
                if (configCount > expectedNumConfigs) {
                    println("Got more configs than $expectedNumConfigs")
                    return null
                }

                val configArgByte = buffer.get()
                val configArg = ConfigArgEnum.fromCode(configGroup, configArgByte)

                if (configArg == null) {
                    println("Unknown config ${String.format("0x%02x", configArgByte)} for $configGroup")
                }

                val configTypeByte = buffer.get()
                val configType = ConfigTypeEnum.fromValue(configTypeByte)

                if (configType == null) {
                    println("Unknown type ${String.format("0x%02x", configTypeByte)} for $configArg")

                    return prefs
                }

                configArg?.getConfigType(groupVersions)?.let {
                    if (configType != it) println("Unexpected arg type $configType for $configArg, expected $it")
                }

                var argsPrefs: Map<String, Any?>? = null

                when (configType) {
                    ConfigTypeEnum.BOOL -> {
                        val value = ConfigBoolean.consume(buffer)

                        println("Got $configArg (${String.format("0x%02x", configArgByte)}) = $value")
                        configArg?.let { argsPrefs = convertBooleanToPrefs(configArg, value) }
                    }

                    ConfigTypeEnum.STRING -> {
                        val value = ConfigString.consume(buffer, includesConstraints)

                        if (value == null) {
                            println("Failed to parse $configType for $configArg")
                            return prefs
                        }

                        println("Got $configArg (${String.format("0x%02x", configArgByte)}) = $value")
                        configArg?.let { argsPrefs = convertStringToPrefs(configArg, value) }
                    }

                    else -> {
                        println("No parser for $configArg")
                        return prefs
                    }
                }

                if (argsPrefs == null) {
                    println("Unhandled $configType pref of type $configArg")
                }

                if (configArg != null && argsPrefs != null && configType == configArg.getConfigType(groupVersions)) {
                    prefs[WearablePreferenceConst.getPrefKnownConfig(configArg.name)] = true
                    prefs.putAll(argsPrefs!!)
                }

                configCount++
            }

            return prefs
        }

        private fun singletonMap(key: String?, value: Any?): Map<String, Any?> {
            if (key == null) return Collections.emptyMap()

            return mapOf(Pair(key, value))
        }
    }

    inner class ConfigSetter {
        private val arguments = LinkedHashMap<ConfigGroupEnum, MutableMap<ConfigArgEnum, ByteArray>>()

        private fun checkArg(arg: ConfigArgEnum, expectedConfigType: ConfigTypeEnum) {
            if (arg.getConfigType(groupVersions) == null) return

            if (expectedConfigType != arg.getConfigType(groupVersions)) {
                throw IllegalArgumentException("Invalid arg type $expectedConfigType fro $arg, expected ${arg.getConfigType(groupVersions)}")
            }
        }

        private fun encode(configGroup: ConfigGroupEnum): ByteArray {
            val baos = ByteArrayOutputStream()
            val configArgMap = arguments[configGroup]!!

            try {
                baos.write(CMD_SET.toInt())
                baos.write(configGroup.value.toInt())
                baos.write(configGroup.version.toInt())
                baos.write(0x00)
                baos.write(configArgMap.size)

                configArgMap.entries.forEach {
                    val configType = it.key.getConfigType(groupVersions)
                    baos.write(it.key.code.toInt())
                    baos.write(configType.value.toInt())
                    baos.write(it.value)
                }
            } catch (e: IOException) {
                println("Failed to encode command: $e")
            }

            return baos.toByteArray()
        }

        private fun putArgument(arg: ConfigArgEnum, encodedBytes: ByteArray) {
            val groupMap: MutableMap<ConfigArgEnum, ByteArray>

            if (arguments.containsKey(arg.configGroup)) {
                groupMap = arguments[arg.configGroup]!!
            } else {
                groupMap = LinkedHashMap()
                arguments[arg.configGroup] = groupMap
            }

            groupMap[arg] = encodedBytes
        }

        fun setBoolean(arg: ConfigArgEnum, value: Boolean): ConfigSetter {
            checkArg(arg, ConfigTypeEnum.BOOL)
            putArgument(arg, byteArrayOf(if (value) 0x01 else 0x00))

            return this
        }

        fun write(builder: TransactionBuilder) {
            arguments.keys.forEach {
                this@ZeppOsConfigService.write(builder, encode(it))
            }
        }
    }

    private class ConfigString(val value: String?, private val maxLength: Int) {
        companion object {
            fun consume(buffer: ByteBuffer, includesConstraints: Boolean): ConfigString? {
                val value = StringUtils.untilNullTerminator(buffer)

                if (value == null) {
                    println("Null terminator not found in buffer")
                    return null
                }

                if (!includesConstraints) return ConfigString(value, -1)

                return ConfigString(value, buffer.get().toInt() and 0xff)
            }
        }
    }

    companion object {
        const val CMD_ACK = 0x06.toByte()
        const val CMD_CAPABILITIES_RESPONSE = 0x02.toByte()
        const val CMD_RESPONSE = 0x04.toByte()
        const val CMD_SET = 0x05.toByte()
        const val ENDPOINT = 0x000a.toShort()

        private val PREF_TO_CONFIG = object : HashMap<String, ConfigArgEnum>() {
            init {
                for (arg in ConfigArgEnum.entries) {
                    if (arg.prefKey != null) {
                        if (containsKey(arg.prefKey)) continue

                        put(arg.prefKey, arg)
                    }
                }
            }
        }
    }
}