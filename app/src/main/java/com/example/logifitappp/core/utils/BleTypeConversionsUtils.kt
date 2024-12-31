package com.example.logifitappp.core.utils

import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.icu.util.TimeZone
import java.nio.charset.StandardCharsets

object BleTypeConversionsUtils {
    const val TZ_FLAG_INCLUDE_DST_IN_TZ = 1

    fun calendarToRawBytes(timestamp: Calendar): ByteArray {
        val year: ByteArray = fromUint16(timestamp[java.util.Calendar.YEAR])
        return byteArrayOf(
            year[0],
            year[1],
            fromUint8(timestamp[java.util.Calendar.MONTH] + 1),
            fromUint8(timestamp[java.util.Calendar.DATE]),
            fromUint8(timestamp[java.util.Calendar.HOUR_OF_DAY]),
            fromUint8(timestamp[java.util.Calendar.MINUTE]),
            fromUint8(timestamp[java.util.Calendar.SECOND]),
            dayOfWeekToRawBytes(timestamp),
            0
        )
    }

    private fun dayOfWeekToRawBytes(calendar: Calendar): Byte {
        return when (val value = calendar[Calendar.DAY_OF_WEEK]) {
            Calendar.SUNDAY -> 7
            else -> (value - 1).toByte()
        }
    }

    fun fromUint16(value: Int): ByteArray {
        return byteArrayOf(
            (value and 0xff).toByte(),
            ((value shr 8) and 0xff).toByte(),
        )
    }

    fun fromUint32(value: Int): ByteArray {
        return byteArrayOf(
            (value and 0xff).toByte(),
            ((value shr 8) and 0xff).toByte(),
            ((value shr 16) and 0xff).toByte(),
            ((value shr 24) and 0xff).toByte(),
        )
    }

    fun fromUint8(value: Int): Byte {
        return (value and 0xff).toByte()
    }

    fun join(start: ByteArray?, end: ByteArray?): ByteArray? {
        if (start == null || start.isEmpty()) {
            return end
        }
        if (end == null || end.isEmpty()) {
            return start
        }

        val result = ByteArray(start.size + end.size)
        System.arraycopy(start, 0, result, 0, start.size)
        System.arraycopy(end, 0, result, start.size, end.size)
        return result
    }

    fun mapTimeZone(calendar: Calendar, timezoneFlags: Int): Byte {
        var offsetMillis = calendar.timeZone.rawOffset

        if (timezoneFlags == TZ_FLAG_INCLUDE_DST_IN_TZ) {
            offsetMillis = calendar.timeZone.getOffset(calendar.timeInMillis)
        }

        return (offsetMillis / (1000 * 60 * 15)).toByte()
    }

    fun rawBytesToCalendar(value: ByteArray): GregorianCalendar {
        if (value.size >= 7) {
            val year = toUint16(value[0], value[1])

            val timestamp = GregorianCalendar(
                year,
                (value[2].toInt() and 0xff) - 1,
                value[3].toInt() and 0xff,
                value[4].toInt() and 0xff,
                value[5].toInt() and 0xff,
                value[6].toInt() and 0xff
            )

            if (value.size > 7) {
                val timeZone = TimeZone.getTimeZone("UTC")
                timeZone.rawOffset = value[7] * 15 * 60 * 1000
                timestamp.timeZone = timeZone
            }

            return timestamp
        }

        return GregorianCalendar()
    }

    fun shortCalendarToRawBytes(timestamp: Calendar): ByteArray {
        val year: ByteArray = fromUint16(timestamp[Calendar.YEAR])

        return byteArrayOf(
            year[0],
            year[1],
            fromUint8(timestamp[Calendar.MONTH] + 1),
            fromUint8(timestamp[Calendar.DATE]),
            fromUint8(timestamp[Calendar.HOUR_OF_DAY]),
            fromUint8(timestamp[Calendar.MINUTE])
        )
    }

    fun toUint16(bytes: ByteArray, offset: Int): Int {
        return (bytes[offset].toInt() and 0xff) or ((bytes[offset + 1].toInt() and 0xff) shl 0xff)
    }

    fun toUint16(vararg bytes: Byte): Int {
        return (bytes[0].toInt() and 0xff) or ((bytes[1].toInt() and 0xff) shl 8)
    }

    fun toUint32(bytes: ByteArray, offset: Int): Int {
        return (bytes[offset].toInt() and 0xff) or ((bytes[offset + 1].toInt() and 0xff) shl 8) or ((bytes[offset + 2].toInt() and 0xff) shl 16) or ((bytes[offset + 3].toInt() and 0xff) shl 24)
    }

    fun toUint32(vararg bytes: Byte): Int {
        return (bytes[0].toInt() and 0xff) or ((bytes[1].toInt() and 0xff) shl 8) or ((bytes[2].toInt() and 0xff) shl 16) or ((bytes[3].toInt() and 0xff) shl 24)
    }

    fun toUtf8s(message: String): ByteArray {
        return message.toByteArray(StandardCharsets.UTF_8)
    }

    fun writeUint16(bytes: ByteArray, offset: Int, value: Int) {
        bytes[offset] = value.toByte()
        bytes[offset + 1] = (value shr 8).toByte()
    }
}