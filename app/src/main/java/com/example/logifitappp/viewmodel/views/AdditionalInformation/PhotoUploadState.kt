package com.example.logifitappp.viewmodel.views.AdditionalInformation

import android.net.Uri

data class PhotoUploadState(
    val photoUri: Uri? = null,
    val photoBase64: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)