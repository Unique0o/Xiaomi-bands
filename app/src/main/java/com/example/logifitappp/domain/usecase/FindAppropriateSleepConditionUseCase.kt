package com.example.logifitappp.domain.usecase

import com.example.logifitappp.core.App
import com.example.logifitappp.core.utils.ColorUtils
import com.example.logifitappp.data.models.SleepConditionModel
import com.example.logifitappp.data.models.TenantModel
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Lime70
import com.example.logifitappp.ui.theme.Orange170
import com.example.logifitappp.ui.theme.Rose120

class FindAppropriateSleepConditionUseCase {
    operator fun invoke(seconds: Long, tenant: TenantModel): SleepConditionModel {
        val condition = App.database.sleepConditionDao().findAppropriate(seconds, tenant.id)

        if (condition != null) return condition

        val sleepAnalysisSeconds = tenant.sleepAnalysisHours / 3600
        val isUnfit = sleepAnalysisSeconds > seconds

        return SleepConditionModel(
            backgroundColor = ColorUtils.toHex(if (isUnfit) Orange170 else Lime70),
            color = ColorUtils.toHex(if (isUnfit) Rose120 else Green298),
            name = if (isUnfit) "no apto" else "apto",
            endSeconds = 0L,
            id = 0,
            startSeconds = 0L,
            tenantId = tenant.id,
        )
    }
}