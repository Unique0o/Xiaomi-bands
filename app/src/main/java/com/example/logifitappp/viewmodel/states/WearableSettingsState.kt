package com.example.logifitappp.viewmodel.states

import com.example.logifitappp.core.wearebles.Wearable

data class WearableSettingsState(
    var shouldShowUnpairWearableModal: Boolean = false,
    var wearable: Wearable? = null
)
