package com.example.logifitappp.enums

import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.Loader
import com.example.logifitappp.ui.components.lottie.AnimatedErrorSignal
import com.example.logifitappp.ui.components.lottie.AnimatedWarningSignal

enum class AppStatusCodeEnum(
    private val code: Int,
    @StringRes  val message: Int,
    val keepOpen: Boolean,
    val component: (@Composable () -> Unit)? = null,
) {
    HELP_ALREADY_STORED(406, R.string.help_already_stored_message, false, { AnimatedWarningSignal() }),
    LOGGING_IN(31, R.string.logging_in_message, true, { Loader() }),
    NO_ASSOCIATED_USER(422, R.string.unprocessable_http_error_message, false, { AnimatedErrorSignal() }),

    NO_INTERNET_CONNECTION(34, R.string.no_internet_connection_message, false, {
        Icon(contentDescription = null, painter = painterResource(id = R.drawable.ic_disconnected_error))
    }),

    PASSWORD_NOT_VALIDATED(105, R.string.password_not_validated_error_message, false, {
        Icon(contentDescription = null, painter = painterResource(id = R.drawable.ic_authentication_error))
    }),

    REQUIRE_SUBSCRIPTION_UPGRADING(402, R.string.require_subscription_upgrading_message, false, { AnimatedWarningSignal() }),
    SERVER_ERROR(500, R.string.server_error_message, false),
    UNKNOWN_ERROR(-1, R.string.unknown_error_message, false),

    UNREGISTERED_USER(104, R.string.unregistered_user_error_message, false, {
        Icon(contentDescription = null, painter = painterResource(id = R.drawable.ic_unregistered_user_error))
    });

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