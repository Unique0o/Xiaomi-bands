package com.example.logifitappp.enums

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.Loader
import com.example.logifitappp.ui.components.lottie.AnimatedBluetoothConnection
import com.example.logifitappp.ui.components.lottie.AnimatedErrorSignal
import com.example.logifitappp.ui.components.lottie.AnimatedSuccessSignal
import com.example.logifitappp.ui.components.lottie.AnimatedWarningSignal
import com.example.logifitappp.ui.components.lottie.AnimatedWearableDataExtraction

enum class AppStatusCodeEnum(
    private val code: Int,
    @StringRes  val message: Int,
    val keepOpen: Boolean,
    val component: (@Composable () -> Unit)? = null,
) {
    CONNECTING_WITH_WEARABLE(3, R.string.connecting_whit_wearable_message, true, { AnimatedBluetoothConnection() }),
    EXTRACTING_WEARABLE_INFORMATION(6, R.string.extracting_wearable_information_message, true, { AnimatedWearableDataExtraction() }),
    FAILED_PASSWORD_RECOVERY(16, R.string.failed_password_recovery_message, false, { AnimatedErrorSignal() }),

    FAILED_WEARABLE_PAIRING(18, R.string.failed_wearable_pairing_message, false, {
        Image(contentDescription = null, painter = painterResource(id = R.drawable.ic_disconnected_error))
    }),

    HELP_ALREADY_STORED(406, R.string.help_already_stored_message, false, { AnimatedWarningSignal() }),

    INTERRUPTED_SYNCHRONIZATION(29, R.string.interrupted_synchronization_message, false, {
        Image(contentDescription = null, painter = painterResource(id = R.drawable.ic_interrupted_synchronization_error))
    }),

    INVALID_WEARABLE_AUTHENTICATION_KEY(30, R.string.invalid_wearable_authentication_key_message, false, {
        Image(contentDescription = null, painter = painterResource(id = R.drawable.ic_authentication_error))
    }),

    LOGGING_IN(31, R.string.logging_in_message, true, { Loader() }),
    NO_ASSOCIATED_USER(422, R.string.unprocessable_http_error_message, false, { AnimatedErrorSignal() }),

    NO_INTERNET_CONNECTION(34, R.string.no_internet_connection_message, false, {
        Image(contentDescription = null, painter = painterResource(id = R.drawable.ic_disconnected_error))
    }),

    NO_SYNCHRONIZED_WEARABLE_DATA(303, R.string.no_synchronized_wearable_data_message, false, {
        Image(contentDescription = null, painter = painterResource(id = R.drawable.ic_no_sleep_data_error))
    }),

    PASSWORD_NOT_VALIDATED(105, R.string.password_not_validated_error_message, false, {
        Image(contentDescription = null, painter = painterResource(id = R.drawable.ic_authentication_error))
    }),

    RECOVERING_PASSWORD(109, R.string.recovering_password_message, true, { Loader() }),
    REQUIRE_SUBSCRIPTION_UPGRADING(402, R.string.require_subscription_upgrading_message, false, { AnimatedWarningSignal() }),
    SERVER_ERROR(500, R.string.server_error_message, false),
    SUCCESSFUL_PASSWORD_RECOVERY(119, R.string.successful_password_recovery_message, false, { AnimatedSuccessSignal() }),
    UNKNOWN_ERROR(-1, R.string.unknown_error_message, false),

    UNREGISTERED_USER(104, R.string.unregistered_user_error_message, false, {
        Image(contentDescription = null, painter = painterResource(id = R.drawable.ic_unregistered_user_error))
    }),

    UNSELECTED_SHIFT(35, R.string.unselected_shift_message, false, {
        Image(contentDescription = null, painter = painterResource(id = R.drawable.ic_unselected_shift_error))
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