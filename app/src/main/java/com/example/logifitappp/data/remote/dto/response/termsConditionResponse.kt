package com.example.logifitappp.data.remote.dto.response

import com.example.logifitappp.data.models.TermsAndConditions

data class TermsAndConditionsResponse(
    val termsAndConditions: TermsAndConditions,
    val generalResponse: GeneralResponse
)