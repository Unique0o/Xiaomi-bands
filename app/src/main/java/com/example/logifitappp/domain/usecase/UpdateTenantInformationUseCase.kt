package com.example.logifitappp.domain.usecase

import com.example.logifitappp.core.App
import com.example.logifitappp.data.remote.dto.response.getGroupModels
import com.example.logifitappp.data.remote.dto.response.getLocationModels
import com.example.logifitappp.data.remote.dto.response.getRosterLocationModels
import com.example.logifitappp.data.remote.dto.response.getShiftModels
import com.example.logifitappp.data.remote.dto.response.getSleepConditionModels
import com.example.logifitappp.data.remote.dto.response.toTenantModel
import com.example.logifitappp.domain.service.EvaluationService
import com.example.logifitappp.domain.service.TenantService
import javax.inject.Inject

class UpdateTenantInformationUseCase @Inject constructor(
    private val tenantService: TenantService,
    private val evaluationService: EvaluationService
) {
    suspend operator fun invoke() {
        val tenant = tenantService.fetch()
        val restParameters = tenantService.fetchRestParameters()
        val evaluations = evaluationService.fetchResults()

        App.database.tenantDao().store(tenant.toTenantModel())
        App.database.groupDao().replaceAll(tenant.data.id, *tenant.getGroupModels().toTypedArray())
        App.database.locationDao().replaceAll(tenant.data.id, *tenant.getLocationModels().toTypedArray())
        App.database.rosterLocationDao().replaceAll(tenant.data.id, *tenant.getRosterLocationModels().toTypedArray())
        App.database.shiftDao().replaceAll(tenant.data.id, *tenant.getShiftModels().toTypedArray())
        App.database.sleepConditionDao().replaceAll(tenant.data.id, *tenant.getSleepConditionModels().toTypedArray())
        App.database.restParameterDao().replaceAll(tenant.data.id, *restParameters.toTypedArray())
        App.database.evaluationResultDao().replaceAll(*evaluations.toTypedArray())
    }
}