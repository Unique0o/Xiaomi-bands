package com.example.logifitappp.viewmodel.states

import com.example.logifitappp.data.remote.dto.response.FetchTrainingResponse
import com.example.logifitappp.enums.AppStatusCodeEnum

data class TrainingDetailState(
    val hasDownloadCertificateFailed: Boolean = false,
    val hasFetchTrainingInformationFailed: Boolean = false,
    val isDownloadingCertificate: Boolean = false,
    val isFetchingTrainingInformation: Boolean = true,
    val status: AppStatusCodeEnum = AppStatusCodeEnum.DOWNLOADING_TRAINING_CERTIFICATE,
    var training: FetchTrainingResponse? = null
)
