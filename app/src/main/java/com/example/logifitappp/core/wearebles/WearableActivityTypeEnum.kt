package com.example.logifitappp.core.wearebles

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.example.logifitappp.R
import com.example.logifitappp.ui.theme.Blue690
import com.example.logifitappp.ui.theme.Orange390
import com.example.logifitappp.ui.theme.Sky320
import com.example.logifitappp.ui.theme.Violet500

enum class WearableActivityTypeEnum(
    private val code: Int,
    @StringRes private val label: Int,
    val color: Color
) {
    ACTIVITY(0x00000001, R.string.activity, Orange390),
    DEEP_SLEEP(0x00000004, R.string.deep_sleep, Blue690),
    LIGHT_SLEEP(0x00000002, R.string.light_sleep, Sky320),
    NOT_WORN(0x00000008, R.string.not_worn, Orange390),
    REM_SLEEP(0x01000000, R.string.rem_sleep, Violet500),
    UNKNOWN(0x00000000, R.string.unknown_activity, Orange390);

    fun getCode() = code

    companion object {
        fun fromCode(code: Int): WearableActivityTypeEnum {
            entries.forEach {
                if (it.code == code) return it
            }

            return UNKNOWN
        }
    }
}