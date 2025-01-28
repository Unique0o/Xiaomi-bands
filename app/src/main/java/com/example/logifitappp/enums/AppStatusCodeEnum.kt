package com.example.logifitappp.enums

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.logifitappp.R
import com.example.logifitappp.ui.components.Loader
import com.example.logifitappp.ui.components.lottie.AnimatedBluetoothConnection
import com.example.logifitappp.ui.components.lottie.AnimatedDataTransfer
import com.example.logifitappp.ui.components.lottie.AnimatedErrorSignal
import com.example.logifitappp.ui.components.lottie.AnimatedSuccessSignal
import com.example.logifitappp.ui.components.lottie.AnimatedWarningSignal
import com.example.logifitappp.ui.components.lottie.AnimatedWearableDataExtraction

enum class AppStatusCodeEnum(
    private val code: Int,
    @StringRes val message: Int,
    val keepOpen: Boolean,
    val component: (@Composable () -> Unit)? = null,
) {
    ASSOCIATING_WORKER(1, R.string.associating_worker_message, true, { Loader() }),
    BAND_THEFT(311, R.string.band_theft_message, false, { AnimatedWarningSignal() }),
    CONNECTING_WITH_WEARABLE(3, R.string.connecting_whit_wearable_message, true, { AnimatedBluetoothConnection() }),

    DISABLED_BLUETOOTH(4, R.string.disabled_bluetooth_message, false, {
        Image(contentDescription = null, painter = painterResource(id = R.drawable.ic_disconnected_error))
    }),

    DISABLED_LOCATION(314, R.string.disabled_location_message, false, {
        Image(contentDescription = null, painter = painterResource(id = R.drawable.ic_disconnected_error))
    }),

    DOWNLOADING_TRAINING_CERTIFICATE(308, R.string.downloading_training_certificate_message, true, { Loader() }),
    DOWNLOADING_EVALUATION_RESULT(305, R.string.downloading_evaluation_result_message, true, { Loader() }),
    EXTRACTING_WEARABLE_INFORMATION(6, R.string.extracting_wearable_information_message, true, { AnimatedWearableDataExtraction() }),
    FAILED_EVALUATION_STORE(313, R.string.failed_evaluation_store_message, false, { AnimatedErrorSignal() }),
    FAILED_PASSWORD_RECOVERY(16, R.string.failed_password_recovery_message, false, { AnimatedErrorSignal() }),
    FAILED_PROFILE_PHOTO_STORE(317, R.string.failed_profile_photo_store_message, false, { AnimatedErrorSignal() }),
    FAILED_ROSTER_INFORMATION_STORAGE(309, R.string.failed_roster_information_storage_message, false, { AnimatedErrorSignal() }),

    FAILED_WEARABLE_PAIRING(18, R.string.failed_wearable_pairing_message, false, {
        Image(contentDescription = null, painter = painterResource(id = R.drawable.ic_disconnected_error))
    }),

    FETCHING_WORKER_LIST(25, R.string.fetching_worker_list_message, true, { Loader() }),

    FINDING_SMART_BAND(26, R.string.finding_smart_band_message, false, {
        Image(contentDescription = null, painter = painterResource(id = R.drawable.ic_find_smart_band))
    }),

    HELP_ALREADY_STORED(406, R.string.help_already_stored_message, false, { AnimatedWarningSignal() }),
    INACTIVE_TENANT(106, R.string.inactive_tenant_message, false, { AnimatedErrorSignal() }),
    INACTIVE_USER(107, R.string.inactive_user_message, false, { AnimatedErrorSignal() }),

    INTERRUPTED_SYNCHRONIZATION(29, R.string.interrupted_synchronization_message, false, {
        Image(contentDescription = null, painter = painterResource(id = R.drawable.ic_interrupted_synchronization_error))
    }),

    INVALID_WEARABLE_AUTHENTICATION_KEY(30, R.string.invalid_wearable_authentication_key_message, false, {
        Image(contentDescription = null, painter = painterResource(id = R.drawable.ic_authentication_error))
    }),

    LOGGING_IN(31, R.string.logging_in_message, true, { Loader() }),
    NO_ASSOCIATED_USER(422, R.string.no_associated_user_error_message, false, { AnimatedErrorSignal() }),

    NO_INTERNET_CONNECTION(34, R.string.no_internet_connection_message, false, {
        Image(contentDescription = null, painter = painterResource(id = R.drawable.ic_disconnected_error))
    }),

    NO_SYNCHRONIZED_WEARABLE_DATA(302, R.string.no_synchronized_wearable_data_message, false, {
        Image(contentDescription = null, painter = painterResource(id = R.drawable.ic_no_sleep_data_error))
    }),

    NO_WEARABLE_SLEEP_DATA(303, R.string.no_wearable_sleep_data_message, false, {
        Image(contentDescription = null, painter = painterResource(id = R.drawable.ic_no_sleep_data_error))
    }),

    PASSWORD_NOT_VALIDATED(105, R.string.password_not_validated_error_message, false, {
        Image(contentDescription = null, painter = painterResource(id = R.drawable.ic_authentication_error))
    }),

    RECOVERING_PASSWORD(109, R.string.recovering_password_message, true, { Loader() }),
    REQUIRE_SUBSCRIPTION_UPGRADING(402, R.string.require_subscription_upgrading_message, false, { AnimatedWarningSignal() }),
    SERVER_ERROR(500, R.string.server_error_message, false),
    SHARING_WITHOUT_SYNCHRONIZATION_TO_LOGIFIT(310, R.string.sharing_without_synchronization_to_logifit_message, false, { AnimatedWarningSignal() }),
    STORING_ADDITIONAL_INFORMATION(315, R.string.storing_additional_information_message, true, { Loader() }),
    STORING_EVALUATION(312, R.string.storing_evaluation_message, true, { Loader() }),
    STORING_PROFILE_PHOTO(316, R.string.storing_profile_photo_message, true, { Loader() }),
    STORING_ROSTER_INFORMATION(306, R.string.storing_roster_information_message, true, { Loader() }),
    SUCCESSFUL_PASSWORD_RECOVERY(119, R.string.successful_password_recovery_message, false, { AnimatedSuccessSignal() }),
    SUCCESSFUL_ROSTER_INFORMATION_STORAGE(307, R.string.successful_roster_information_storage_message, false, { AnimatedSuccessSignal() }),
    SUCCESSFUL_WEARABLE_INFORMATION_SYNCHRONIZING(123, R.string.successful_wearable_information_synchronizing_message, false, { AnimatedSuccessSignal() }),
    SUCCESSFUL_WEARABLE_INFORMATION_TRANSFERRING(122, R.string.successful_wearable_information_transferring_message, false, { AnimatedSuccessSignal() }),
    SUCCESSFUL_WORKER_ASSOCIATED(124, R.string.successful_worker_associated_message, false, { AnimatedSuccessSignal() }),
    TRANSFERRING_WEARABLE_INFORMATION(126, R.string.transferring_wearable_information_message, true, { AnimatedDataTransfer() }),
    UNKNOWN_ERROR(-1, R.string.unknown_error_message, false),
    UNPROCESSABLE_WEARABLE_INFORMATION_TRANSFER(130, R.string.unprocessable_wearable_information_transfer_message, false, { AnimatedWarningSignal() }),

    UNREGISTERED_USER(104, R.string.unregistered_user_error_message, false, {
        Image(contentDescription = null, painter = painterResource(id = R.drawable.ic_unregistered_user_error))
    }),

    UNSELECTED_SHIFT(35, R.string.unselected_shift_message, false, {
        Image(contentDescription = null, painter = painterResource(id = R.drawable.ic_unselected_shift_error))
    }),

    UNSUPPORTED_WEARABLE(300, R.string.unsupported_wearable_message, false, { AnimatedErrorSignal() }),
    WORKER_LIST_EMPTY(318, R.string.worker_list_empty_message, false, { AnimatedWarningSignal() });

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