package com.example.logifitappp.viewmodel.views

import android.icu.util.GregorianCalendar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.R
import com.example.logifitappp.core.App
import com.example.logifitappp.core.App.Companion.context
import com.example.logifitappp.core.AppPreferences
import com.example.logifitappp.core.utils.DateTimeUtils
import com.example.logifitappp.data.models.RosterLocationModel
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.data.remote.dto.requests.StoreRosterRequest
import com.example.logifitappp.domain.service.UserService
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import com.example.logifitappp.viewmodel.states.RosterRecordingState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = RosterRecordingViewModel.RosterRecordingViewModelFactory::class)
class RosterRecordingViewModel @AssistedInject constructor(
    @Assisted private val user: UserModel?,
    private val userService: UserService
): ViewModel() {
    @AssistedFactory
    interface RosterRecordingViewModelFactory {
        fun create(user: UserModel?): RosterRecordingViewModel
    }

    var locations = mutableListOf<RosterLocationModel>()
        private set

    var state by mutableStateOf(RosterRecordingState())
        private set

    init {
        user?.let { locations.addAll(App.database.rosterLocationDao().all(it.tenantId)) }
    }

    fun saveRoster() {
        if (user == null) return

        if (!validateInputsNotEmpty()) return

        viewModelScope.launch {
            try {
                state = state.copy(
                    isLoading = true,
                    status = AppStatusCodeEnum.STORING_ROSTER_INFORMATION
                )

                userService.storeRosterInformation(StoreRosterRequest(
                    comentario = state.comment.text,
                    fecha_retorno = DateTimeUtils.format(state.startDate!!.time, "yyyy-MM-dd"),
                    fecha_salida = DateTimeUtils.format(state.endDate!!.time, "yyyy-MM-dd"),
                    location_id = state.location!!.id,
                    user_id = user.id
                ))

                state = state.copy(
                    status = AppStatusCodeEnum.SUCCESSFUL_ROSTER_INFORMATION_STORAGE,
                    hasRosterInformationStorageBeenSuccessful = true
                )

                App.preferences.getPreferences()
                    .edit()
                    .putBoolean(AppPreferences.NEW_ROSTER, true)
                    .apply()
            } catch (e: HttpConsumerException) {
                state = state.copy(
                    status = e.getStatus(),
                    hasRosterInformationStorageBeenSuccessful = false
                )
            }
        }
    }

    fun stopProcessing() {
        state = state.copy(isLoading = false)
    }

    fun updateComment(comment: TextFieldValue) {
        state = state.copy(comment = comment)
    }

    fun updateLocation(location: RosterLocationModel) {
        state = state.copy(location = location)
    }

    fun updateRangeDate(start: Long?, end: Long?) {
        state = state.copy(
            endDate = end?.let {
                GregorianCalendar.getInstance().apply {
                    timeInMillis = it
                }
            },

            startDate = start?.let {
                GregorianCalendar.getInstance().apply {
                    timeInMillis = it
                }
            }
        )
    }

    private fun validateInputsNotEmpty(): Boolean {
        val isRangeDateValid = state.endDate != null && state.startDate != null
        val isLocationValid = state.location != null

        state = state.copy(
            endDateError = if (isRangeDateValid) null else context.getString(R.string.range_date_validation_error_message),
            locationError = if (isLocationValid) null else context.getString(R.string.location_validation_error_message)
        )

        return isRangeDateValid && isLocationValid
    }
}