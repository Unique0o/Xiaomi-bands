package com.example.logifitappp.viewmodel.views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logifitappp.core.App
import com.example.logifitappp.core.AppPreferences
import com.example.logifitappp.data.models.UserModel
import com.example.logifitappp.domain.service.UserService
import com.example.logifitappp.viewmodel.states.RosterState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = RosterViewModel.RosterViewModelFactory::class)
class RosterViewModel @AssistedInject constructor(
    @Assisted private val user: UserModel?,
    private val userService: UserService
): ViewModel() {
    @AssistedFactory
    interface RosterViewModelFactory {
        fun create(user: UserModel?): RosterViewModel
    }

    var state by mutableStateOf(RosterState())
        private set

    init {
        fetchRosterPage()
    }

    fun checkForNewRoster() {
        if (App.preferences.getPreferences().getBoolean(AppPreferences.NEW_ROSTER, false)) {
            state = state.copy(roster = null)
            fetchRosterPage()
        }
    }

    fun fetchRosterPage() {
        if (user == null) return

        if (state.roster != null && state.roster!!.currentPage == state.roster!!.lastPage) return

        viewModelScope.launch {
            try {
                state = state.copy(isLoading = true)

                val response = userService.fetchViewDetails(user.id, (state.roster?.currentPage ?: 0) + 1)
                state = state.copy(roster = response.roster)
            } catch (e: Exception) {
                state = state.copy(hasFetchRosterDetailFailed = true)
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }

    fun markAsRefresh() {
        App.preferences.getPreferences()
            .edit()
            .remove(AppPreferences.NEW_ROSTER)
            .apply()
    }
}