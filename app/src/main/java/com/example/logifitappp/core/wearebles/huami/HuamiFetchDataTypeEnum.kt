package com.example.logifitappp.core.wearebles.huami

enum class HuamiFetchDataTypeEnum(val code: Byte) {
    ACTIVITY(0x01),
    MANUAL_HEART_RATE(0x02),
    SPORTS_SUMMARIES(0x05),
    SPORTS_DETAILS(0x06),
    DEBUG_LOGS(0x07),
    PAI(0x0d),
    STRESS_MANUAL(0x12),
    STRESS_AUTOMATIC(0x13),
    SPO2_NORMAL(0x25),
    SPO2_SLEEP(0x26),
    STATISTICS(0x2c),
    TEMPERATURE(0x2e),
    SLEEP_RESPIRATORY_RATE(0x38),
    RESTING_HEART_RATE(0x3a),
    MAX_HEART_RATE(0x3d);
}