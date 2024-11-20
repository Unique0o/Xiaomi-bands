package com.example.logifitappp.viewmodel.states

import com.example.logifitappp.core.wearebles.Wearable

data class WearableProfileState(
    val activityDataSynchronizationMessage: String = "",
    val synchronizationWithLogifitMessage: String = "",
    val totalSleepTimeMessage: String = "",
    val wearable: Wearable? = null
)
