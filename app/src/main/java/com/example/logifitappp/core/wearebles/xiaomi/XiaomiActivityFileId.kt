package com.example.logifitappp.core.wearebles.xiaomi

import com.example.logifitappp.core.utils.DateTimeUtils
import org.apache.commons.lang3.builder.CompareToBuilder
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.Date

class XiaomiActivityFileId(
    private val timestamp: Date,
    private val timezone: Int,
    private val type: Int,
    private val subtype: Int,
    private val detailType: Int,
    private val version: Int
): Comparable<XiaomiActivityFileId> {
    override fun compareTo(other: XiaomiActivityFileId): Int {
        return CompareToBuilder()
            .append(timestamp, other.timestamp)
            .append(timezone, other.timezone)
            .append(type, other.type)
            .append(subtype, other.subtype)
            .append(getDetailType().getFetchOrder(), other.getDetailType().getFetchOrder())
            .append(version, other.version)
            .build()
    }

    fun getDetailType() = XiaomiActivityDetailTypeEnum.fromCode(detailType)

    fun getSubtype() = XiaomiActivitySubtypeEnum.fromCode(getType(), subtype)

    fun getTimestamp() = timestamp

    fun getType() = XiaomiActivityTypeEnum.fromCode(type)

    fun getVersion() = version

    fun toBytes(): ByteArray {
        val buffer = ByteBuffer.allocate(7).order(ByteOrder.LITTLE_ENDIAN)
        buffer.putInt((timestamp.time / 1000).toInt())
        buffer.put(timezone.toByte())
        buffer.put(version.toByte())
        buffer.put(((type shl 7) or (subtype shl 2) or detailType).toByte())

        return buffer.array()
    }

    override fun toString(): String {
        val mType = XiaomiActivityTypeEnum.fromCode(type)
        val mSubtype = XiaomiActivitySubtypeEnum.fromCode(mType, subtype)
        val mDetailType = XiaomiActivityDetailTypeEnum.fromCode(detailType)

        return "${this::class.simpleName} {" +
                "timestamp=${DateTimeUtils.formatIso8601(timestamp)}, " +
                "timezone=$timezone}," +
                "type=$mType, " +
                "subtype=$mSubtype, " +
                "detailType=$detailType, " +
                "version=$version" +
                "}"
    }

    companion object {
        fun from(bytes: ByteArray): XiaomiActivityFileId {
            return from(ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN))
        }

        fun from(buffer: ByteBuffer): XiaomiActivityFileId {
            val ts = buffer.getInt()
            val tz = buffer.get().toInt()
            val version = buffer.get().toInt()
            val flags = buffer.get().toInt()

            val type = (flags shr 7) and 1
            val subtype = (flags and 127) shr 2
            val detailType = flags and 3

            return XiaomiActivityFileId(Date(ts * 1000L), tz, type, subtype, detailType, version)
        }
    }
}