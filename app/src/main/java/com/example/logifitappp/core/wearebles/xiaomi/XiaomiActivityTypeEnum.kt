package com.example.logifitappp.core.wearebles.xiaomi

enum class XiaomiActivityTypeEnum(private val code: Int) {
    UNKNOWN(-1),
    ACTIVITY(0),
    SPORTS(1);

    fun getCode() = code

    companion object {
        fun fromCode(code: Int): XiaomiActivityTypeEnum {
            entries.forEach {
                if (it.getCode() == code) return it
            }

            return UNKNOWN
        }
    }
}