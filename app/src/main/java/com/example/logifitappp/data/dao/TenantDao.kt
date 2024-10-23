package com.example.logifitappp.data.dao

import androidx.room.Dao
import androidx.room.Upsert
import com.example.logifitappp.data.models.TenantModel

@Dao
abstract class TenantDao {
    @Upsert
    abstract fun store(tenant: TenantModel)
}