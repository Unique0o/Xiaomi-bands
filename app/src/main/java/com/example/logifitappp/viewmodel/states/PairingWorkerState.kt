package com.example.logifitappp.viewmodel.states

import com.example.logifitappp.core.wearebles.Wearable
import com.example.logifitappp.data.remote.dto.response.WorkerItemResponse
import com.example.logifitappp.enums.AppStatusCodeEnum

data class PairingWorkerState(
    var isPairing: Boolean = false,
    var pairingStatus: AppStatusCodeEnum = AppStatusCodeEnum.ASSOCIATING_WORKER,
    var selectedWorker: WorkerItemResponse? = null,
    var wearable: Wearable? = null
)
