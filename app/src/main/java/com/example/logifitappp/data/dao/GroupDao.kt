package com.example.logifitappp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.logifitappp.data.models.GroupModel

@Dao
abstract class GroupDao {
    @Query("SELECT * FROM groups")
    abstract fun all(): List<GroupModel>

    @Query("DELETE FROM groups WHERE tenant_id = :tenantId")
    abstract fun delete(tenantId: Int)

    @Transaction
    open suspend fun replaceAll(tenantId: Int, vararg groups: GroupModel) {
        delete(tenantId)
        store(*groups)
    }

    @Upsert
    abstract fun store(vararg groups: GroupModel)
}