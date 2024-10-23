package com.example.logifitappp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.logifitappp.data.models.RestParameterModel

@Dao
abstract class RestParameterDao {
    @Query("DELETE FROM rest_parameters WHERE tenant_id = :tenantId")
    abstract fun delete(tenantId: Int)

    @Query("SELECT * FROM rest_parameters WHERE tenant_id = :tenantId AND type = :type")
    abstract fun find(type: String, tenantId: Int): RestParameterModel?

    @Upsert
    abstract fun store(vararg restParameters: RestParameterModel)
}