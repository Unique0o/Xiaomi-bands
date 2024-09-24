package com.example.logifitappp.enums

import androidx.annotation.StringRes
import com.example.logifitappp.R

enum class AppStatusCodeEnum(private val code: Int, @StringRes  val message: Int) {
    NO_INTERNET_CONNECTION(34, R.string.no_internet_connection_message),
    PASSWORD_NOT_VALIDATED(105, R.string.password_not_validated_error_message),
    UNREGISTERED_USER(104, R.string.unregistered_user_error_message);

    fun code() = code

    companion object {
        fun fromCode(code: Int): AppStatusCodeEnum {
            entries.forEach {
                if (it.code == code) return it
            }

            return NO_INTERNET_CONNECTION
        }
    }
}