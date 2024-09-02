package com.example.logifitappp.core.wearebles.xiaomi

enum class XiaomiActivityDetailTypeEnum(private val code: Int) {
    UNKNOWN(-1),
    DETAILS(0),
    SUMMARY(0x01),
    GPS_TRACK(0x02);

    fun getCode() = code

    fun getFetchOrder() = when (this) {
        SUMMARY -> 0
        DETAILS -> 1
        GPS_TRACK -> 2
        else -> 3
    }

    companion object {
        fun fromCode(code: Int): XiaomiActivityDetailTypeEnum {
            entries.forEach {
                if (it.getCode() == code) return it
            }

            return UNKNOWN
        }
    }
}