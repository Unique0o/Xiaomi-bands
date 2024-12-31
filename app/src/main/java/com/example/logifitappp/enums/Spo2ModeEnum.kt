package com.example.logifitappp.enums

import androidx.compose.ui.graphics.toArgb
import com.example.logifitappp.ui.theme.Blue690
import com.example.logifitappp.ui.theme.Orange510

enum class Spo2ModeEnum(val color: Int) {
    AWAKE(Orange510.toArgb()),
    ASLEEP(Blue690.toArgb());

    companion object {
        fun fromName(name: String): Spo2ModeEnum {
            Spo2ModeEnum.entries.forEach {
                if (it.name == name) return it
            }

            return AWAKE
        }
    }
}