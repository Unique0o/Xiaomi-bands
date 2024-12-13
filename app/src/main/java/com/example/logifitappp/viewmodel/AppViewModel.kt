package com.example.logifitappp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.core.App
import com.example.logifitappp.data.models.TenantModel
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.domain.usecase.LoadAppWhenAnUserIsAuthenticatedUseCase
import com.example.logifitappp.enums.AppStatusCodeEnum
import com.example.logifitappp.exceptions.HttpConsumerException
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
                    e.printStackTrace()

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

                tenant = user?.tenantId?.let { App.database.tenantDao().find(it) }
            }

            isLoading = false
        }
    }

    fun logout() {
        App.database.userDao().logout()
        user = null
        tenant = null
    }

    fun reloadAuthenticatedUser() {
        user = App.database.userDao().getLoggedIn()
    }

    fun updateUser(user: UserModel) {
        this.user = user
        this.tenant = App.database.tenantDao().find(user.tenantId)
    }
}