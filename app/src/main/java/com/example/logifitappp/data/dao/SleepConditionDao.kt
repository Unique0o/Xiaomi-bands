package com.example.logifitappp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.logifitappp.core.utils.ColorUtils
import com.example.logifitappp.data.models.SleepConditionModel
import com.example.logifitappp.data.models.TenantModel
import com.example.logifitappp.ui.theme.Green298
import com.example.logifitappp.ui.theme.Lime70
import com.example.logifitappp.ui.theme.Orange170
import com.example.logifitappp.ui.theme.Rose120

@Dao
abstract class SleepConditionDao {
    @Query("DELETE FROM sleep_conditions WHERE tenant_id = :tenantId")
    abstract fun delete(tenantId: Int)

    @Query("SELECT * FROM sleep_conditions WHERE tenant_id = :tenantId AND start_seconds <= :seconds AND end_seconds > :seconds LIMIT 1")
    abstract fun findAppropriate(seconds: Long, tenantId: Int): SleepConditionModel?

    @Upsert
    abstract fun store(vararg sleepConditions: SleepConditionModel)

    fun findAppropriate(seconds: Long, tenant: TenantModel): SleepConditionModel {
        val condition = findAppropriate(seconds, tenant.id)

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