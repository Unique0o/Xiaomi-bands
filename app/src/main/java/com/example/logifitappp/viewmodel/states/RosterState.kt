package com.example.logifitappp.viewmodel.states

import com.example.logifitappp.data.remote.dto.response.FetchRosterDetailResponse

data class RosterState(
    val hasFetchRosterDetailFailed: Boolean = false,
    val isLoading: Boolean = false,
    val roster: FetchRosterDetailResponse? = null,
)
