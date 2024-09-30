package com.example.logifitappp.data.repository

import com.example.logifitappp.data.remote.api.HelpApi
import com.example.logifitappp.data.remote.dto.requests.StoreHelpRequest
import com.example.logifitappp.data.remote.dto.response.GeneralResponse
import com.example.logifitappp.domain.repository.HelpRepository
import retrofit2.Response
import javax.inject.Inject

class HelpRepositoryImp @Inject constructor(private val helpApi: HelpApi)
    : HelpRepository {

    override suspend fun store(storeHelpRequest: StoreHelpRequest): Response<GeneralResponse> {
        return helpApi.store(storeHelpRequest)
    }
}