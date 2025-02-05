package com.example.logifitappp.viewmodel.views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.core.App
import com.example.logifitappp.core.AppPreferences
import com.example.logifitappp.data.remote.dto.response.NotificationStructure
import com.example.logifitappp.domain.service.NotificationService
import com.example.logifitappp.viewmodel.states.NotificationsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val notificationService: NotificationService
): ViewModel() {
    var state by mutableStateOf(NotificationsState())
        private set

    var notifications = mutableStateListOf<NotificationStructure>()
        private set

    init {
        fetchNotifications()
    }

    fun fetchNotifications() {
        viewModelScope.launch {
            try {
                state = state.copy(isLoading = true)

                notifications.addAll(notificationService.all())

                state = state.copy(hasFetchNotificationsFailed = false)
            } catch (e: Exception) {
                state = state.copy(hasFetchNotificationsFailed = true)
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }

    fun markAsRead() {
        App.preferences.getPreferences()
            .edit()
            .remove(AppPreferences.NEW_NOTIFICATION)
            .apply()
    }
}