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

        App.database.groupDao().apply {
            delete(tenant.data.id)
            store(*tenant.getGroupModels().toTypedArray())
        }

        App.database.locationDao().apply {
            delete(tenant.data.id)
            store(*tenant.getLocationModels().toTypedArray())
        }

        App.database.rosterLocationDao().apply {
            delete(tenant.data.id)
            store(*tenant.getRosterLocationModels().toTypedArray())
        }

        App.database.shiftDao().apply {
            delete(tenant.data.id)
            store(*tenant.getShiftModels().toTypedArray())
        }

        App.database.sleepConditionDao().apply {
            delete(tenant.data.id)
            store(*tenant.getSleepConditionModels().toTypedArray())
        }

        App.database.restParameterDao().apply {
            delete(tenant.data.id)
            store(*restParameters.toTypedArray())
        }

        App.database.evaluationResultDao().apply {
            delete()
            store(*evaluations.toTypedArray())
        }
    }
}