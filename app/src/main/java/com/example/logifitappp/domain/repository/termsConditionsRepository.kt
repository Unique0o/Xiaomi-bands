package com.example.logifitappp.domain.repository


import com.example.logifitappp.data.remote.dto.response.TermsAndConditionsResponse

interface TermsAndConditionsRepository {
    suspend fun getTermsAndConditions(): Result<TermsAndConditionsResponse>
}
