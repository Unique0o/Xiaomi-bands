package com.example.logifitappp.core.wearebles.xiaomi

enum class XiaomiActivitySubtypeEnum(private val type: XiaomiActivityTypeEnum, private val code: Int) {
    UNKNOWN(XiaomiActivityTypeEnum.UNKNOWN, -1),
    ACTIVITY_DAILY(XiaomiActivityTypeEnum.ACTIVITY, 0x00),
    ACTIVITY_SLEEP_STAGES(XiaomiActivityTypeEnum.ACTIVITY, 0x03),
    ACTIVITY_MANUAL_SAMPLES(XiaomiActivityTypeEnum.ACTIVITY, 0x06),
    ACTIVITY_SLEEP(XiaomiActivityTypeEnum.ACTIVITY, 0x08),
    SPORTS_OUTDOOR_RUNNING(XiaomiActivityTypeEnum.SPORTS, 0x01),
    SPORTS_OUTDOOR_WALKING_V1(XiaomiActivityTypeEnum.SPORTS, 0x02),
    SPORTS_INDOOR_CYCLING(XiaomiActivityTypeEnum.SPORTS, 0x07),
    SPORTS_FREESTYLE(XiaomiActivityTypeEnum.SPORTS, 0x08),
    SPORTS_POOL_SWIMMING(XiaomiActivityTypeEnum.SPORTS, 0x09),
    SPORTS_HIIT(XiaomiActivityTypeEnum.SPORTS, 0x10),
    SPORTS_ELLIPTICAL(XiaomiActivityTypeEnum.SPORTS, 0x0B),
    SPORTS_OUTDOOR_WALKING_V2(XiaomiActivityTypeEnum.SPORTS, 0x16),
    SPORTS_OUTDOOR_CYCLING(XiaomiActivityTypeEnum.SPORTS, 0x17);

    fun getCode() = code

    companion object {
        fun fromCode(type: XiaomiActivityTypeEnum, code: Int): XiaomiActivitySubtypeEnum {
            entries.forEach {
                if (it.type == type && it.getCode() == code) return it
            }

            return UNKNOWN
        }
    }
}