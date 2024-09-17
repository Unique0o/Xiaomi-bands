package com.example.logifitappp.core.wearebles.xiaomi.parsers

import com.example.logifitappp.core.wearebles.xiaomi.XiaomiActivityFileId
import com.example.logifitappp.core.wearebles.xiaomi.XiaomiActivityProvider
import com.example.logifitappp.core.wearebles.xiaomi.XiaomiSupport
import com.example.logifitappp.data.models.XiaomiRawActivityModel
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.Calendar

class XiaomiDailyDetailsParser: XiaomiActivityParser() {
    override fun parse(support: XiaomiSupport, fileId: XiaomiActivityFileId, bytes: ByteArray): Boolean {
        val version = fileId.getVersion()

        val headerSize = when (version) {
            1, 2 -> 4
            3 -> 5
            else -> return false.also { println("Unable to parse daily details version $version") }
        }

        val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)
        buffer.get(ByteArray(7))
        val fileIdPadding = buffer.get()

        if (fileIdPadding != 0.toByte()) println("Expected 0 padding after fileId, got $fileIdPadding - parsing might fail")

        val header = ByteArray(headerSize)
        buffer.get(header)

        println("Daily Details Header: ${header.contentToString()}")

        val complexParser = XiaomiComplexActivityParser(header, buffer)

        val timestamp = Calendar.getInstance()
        timestamp.time = fileId.getTimestamp()

        val samples = mutableListOf<XiaomiRawActivityModel>()

        while (buffer.position() < buffer.limit() - 4) {
            complexParser.reset()

            val sample = XiaomiRawActivityModel()
            sample.timestamp = timestamp.timeInMillis / 1000

            var includeExtraEntry = 0

            if (complexParser.nextGroup(16)) {
                if (complexParser.hasSecond()) includeExtraEntry = complexParser.get(1, 1)

                if (complexParser.hasThrid()) sample.steps = complexParser.get(2, 16)
            }

            if (complexParser.nextGroup(8)) {
                if (complexParser.hasSecond()) complexParser.get(2, 6)
            }

            complexParser.nextGroup(8)
            complexParser.nextGroup(16)

            if (complexParser.nextGroup(8)) {
                if (complexParser.hasFirst()) sample.heartRate = complexParser.get(0, 8)
            }

            if (complexParser.nextGroup(8)) {
                complexParser.hasFirst()
            }

            complexParser.nextGroup(16)

            if (version >= 3) {
                if (complexParser.nextGroup(8)) {
                    if (complexParser.hasFirst()) sample.spo2 = complexParser.get(0, 8)
                }

                if (complexParser.nextGroup(8)) {
                    if (complexParser.hasFirst()) {
                        val stress = complexParser.get(0, 8)

                        if (stress != 255) sample.stress = stress
                    }
                }
            }

            if (includeExtraEntry == 1) complexParser.nextGroup(8)

            samples.add(sample)
            timestamp.add(Calendar.MINUTE, 1)
        }

        try {
            val wearable = support.getWearable()
            val activityProvider = wearable.getWearableCoordinator().getActivityProvider(wearable) as XiaomiActivityProvider

            activityProvider.store(*samples.toTypedArray())

            return true
        } catch (e: Exception) {
            return false.also { println("Error saving activity samples $e") }
        }
    }
}