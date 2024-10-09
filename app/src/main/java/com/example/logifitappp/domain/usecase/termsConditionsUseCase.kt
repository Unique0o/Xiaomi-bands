package com.example.logifitappp.domain.usecase


import com.example.logifitappp.data.remote.dto.response.TermsAndConditionsResponse
import com.example.logifitappp.domain.repository.TermsAndConditionsRepository
import javax.inject.Inject

class GetTermsAndConditionsUseCase @Inject constructor(
    private val repository: TermsAndConditionsRepository
) {
    suspend operator fun invoke(): Result<TermsAndConditionsResponse> {
        return repository.getTermsAndConditions()
    }
}