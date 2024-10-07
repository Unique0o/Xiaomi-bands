package com.example.logifitappp.enums

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.Loader

enum class AppStatusCodeEnum(
    private val code: Int,
    @StringRes  val message: Int,
    val component: (@Composable () -> Unit)? = null,
) {
    LOGGING_IN(31, R.string.logging_in_message, { Loader() }),
    NO_INTERNET_CONNECTION(34, R.string.no_internet_connection_message),
    PASSWORD_NOT_VALIDATED(105, R.string.password_not_validated_error_message),
    SERVER_ERROR(500, R.string.server_error_message),
    UNREGISTERED_USER(104, R.string.unregistered_user_error_message),
    UNKNOWN_ERROR(-1, R.string.unknown_error_message);

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