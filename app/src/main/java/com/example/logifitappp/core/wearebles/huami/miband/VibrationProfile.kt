package com.example.logifitappp.core.wearebles.huami.miband

import com.example.logifitappp.enums.AlertLevelEnum
import com.example.logifitappp.enums.VibrationTypeEnum

class VibrationProfile(val id: String, val onOffSequence: Array<Int>, val repeat: Short) {
    var alertLevel = AlertLevelEnum.MILD_ALERT.id

    init {
        if (onOffSequence.size % 2 != 0) throw IllegalArgumentException("Each on duration must have a subsequent off duration")
    }

    companion object {
        fun getProfile(id: String, repeat: Short) = when (id) {
            VibrationTypeEnum.STACCATO.name.lowercase() -> VibrationProfile(id, arrayOf(100, 0), repeat)
            VibrationTypeEnum.SHORT.name.lowercase() -> VibrationProfile(id, arrayOf(200, 200), repeat)
            VibrationTypeEnum.LONG.name.lowercase() -> VibrationProfile(id, arrayOf(500, 1000), repeat)
            VibrationTypeEnum.WATERDROP.name.lowercase() -> VibrationProfile(id, arrayOf(100, 1500), repeat)
            VibrationTypeEnum.RING.name.lowercase() -> VibrationProfile(id, arrayOf(300, 200, 600, 2000), repeat)
            VibrationTypeEnum.ALARM_CLOCK.name.lowercase() -> VibrationProfile(id, arrayOf(30, 35, 30, 35, 30, 35, 30, 800), repeat)
            else -> VibrationProfile(id, arrayOf(300, 600), repeat)
        }
    }
}