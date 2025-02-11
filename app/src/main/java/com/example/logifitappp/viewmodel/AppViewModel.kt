package com.example.logifitappp.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.core.App
import com.example.logifitappp.core.AppPreferences
import com.example.logifitappp.data.models.OccupationalInfoItemModel
import com.example.logifitappp.data.models.TenantModel
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.domain.usecase.LoadAppWhenAnUserIsAuthenticatedUseCase
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
import com.example.logifitappp.viewmodel.views.OccupationItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Thread.sleep
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val loadAppWhenAnUserIsAuthenticatedUseCase: LoadAppWhenAnUserIsAuthenticatedUseCase
): ViewModel() {
    var isLoading by mutableStateOf(true)
        private set

    var shouldItOmitAdditionalInformation by mutableStateOf(true)
        private set

    var shouldItOmitOnboarding by mutableStateOf(false)
        private set

    var tenant by mutableStateOf<TenantModel?>(null)
        private set

    var user by mutableStateOf<UserModel?>(null)
        private set

    init {
        checkAuthenticatedUser()
    }

    private fun checkAuthenticatedUser() {
        viewModelScope.launch {
            val authenticatedUser = App.database.userDao().getLoggedIn()
            sleep(1500)

            if (authenticatedUser != null) {
                user = try {
                    loadAppWhenAnUserIsAuthenticatedUseCase(authenticatedUser.accessToken)
                } catch (e: HttpConsumerException) {
                    when (e.getStatus()) {
                        AppStatusCodeEnum.INACTIVE_USER -> run inactiveUser@ {
                            val inactiveUser = authenticatedUser.copy(isActive = false)
                            App.database.userDao().store(inactiveUser)

                            return@inactiveUser inactiveUser
                        }

                        AppStatusCodeEnum.INACTIVE_TENANT -> run inactiveTenant@{
                            App.database.tenantDao().inactive(authenticatedUser.tenantId)
                            return@inactiveTenant authenticatedUser
                        }

                        else -> authenticatedUser
                    }
                } catch (e: Exception) {
                    authenticatedUser
                }

                load()
            }

            isLoading = false
        }
    }

    private fun load() {
        tenant = user?.tenantId?.let { App.database.tenantDao().find(it) }
        Log.e("database", "=> ${tenant.toString()}")
        shouldItOmitOnboarding = App.preferences.getBoolean(AppPreferences.OMIT_ONBOARDING, false)
        shouldItOmitAdditionalInformation = App.preferences.getBoolean(AppPreferences.OMIT_ADDITIONAL_INFORMATION, true)
    }

    fun updateUserForSelectorFields(id: String, item: OccupationItem){
        when(id){
            "workload" -> { user = user?.copy(workloadValue = item.id) }
            "occupationalAttentionType" -> { user = user?.copy(attentionValue = item.id) }
        }
        user?.let { App.database.userDao().store(it) }
    }

    fun updateUserForTimerFields(id: String, totalSeconds: Int){
        when(id){
            "timeTravel" -> { user = user?.copy(commutingSeconds = totalSeconds) }
            "breaksFrequency" -> { user = user?.copy(breakFrequencySeconds = totalSeconds) }
            "breaksLength" -> { user = user?.copy(breakAverageSeconds = totalSeconds) }
        }
        user?.let { App.database.userDao().store(it) }
    }

    fun getLocalUserTimerFieldsData(id: String): Int?{
        return App.database.userDao().getLoggedIn()?.let { user ->
            when(id){
                "timeTravel" ->  user.commutingSeconds
                "breaksFrequency" ->  user.breakFrequencySeconds
                "breaksLength" ->  user.breakAverageSeconds
                else -> {null}
            }
        }
    }


    fun fetchUserGroup(tenantId: Int, userGroupId: Int) = App.database.groupDao().all(tenantId).firstOrNull() { groupModel -> groupModel.id == userGroupId }
    fun fetchUserShift(tenantId:Int, userShiftId: Int) = App.database.shiftDao().all(tenantId).firstOrNull { shiftModel -> shiftModel.id == userShiftId }

    fun logout() {
        App.database.userDao().logout()
        user = null
        tenant = null
    }

    fun omitOnboarding() {
        App.preferences
            .getPreferences()
            .edit()
            .putBoolean(AppPreferences.OMIT_ONBOARDING, true)
            .apply()

        shouldItOmitOnboarding = true
    }

    fun reloadAuthenticatedUser() {
        user = App.database.userDao().getLoggedIn()
        tenant = user?.let { App.database.tenantDao().find(it.tenantId) }
    }

    fun updateShouldItOmitAdditionalInformation(shouldOmit: Boolean) {
        shouldItOmitAdditionalInformation = shouldOmit
    }
}