package com.example.logifitappp.domain.usecase


import com.example.logifitappp.data.remote.dto.requests.StoreHelpRequest
import com.example.logifitappp.data.remote.dto.response.GeneralResponse
import com.example.logifitappp.domain.service.HelpService
import javax.inject.Inject

class HelpUseCase @Inject constructor(private val helpService: HelpService) {
    suspend operator fun invoke(contact: String?, message: String?): GeneralResponse {
        return helpService.store(StoreHelpRequest(contact, message))
    }
}