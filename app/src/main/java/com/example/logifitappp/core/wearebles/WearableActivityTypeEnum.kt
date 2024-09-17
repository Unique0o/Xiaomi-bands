package com.example.logifitappp.core.wearebles

import androidx.annotation.StringRes
import com.example.logifitappp.R

enum class WearableActivityTypeEnum(private val code: Int, @StringRes private val label: Int) {
    ACTIVITY(0x00000001, R.string.activity),
    DEEP_SLEEP(0x00000004, R.string.deep_sleep),
    LIGHT_SLEEP(0x00000002, R.string.light_sleep),
    NOT_WORN(0x00000008, R.string.not_worn),
    REM_SLEEP(0x01000000, R.string.rem_sleep),
    UNKNOWN(0x00000000, R.string.unknown_activity);

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