package com.example.logifitappp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.logifitappp.data.models.GroupModel

@Dao
abstract class GroupDao {
    @Query("SELECT * FROM groups")
    abstract fun all(): List<GroupModel>

    @Query("DELETE FROM groups WHERE tenant_id = :tenantId")
    abstract fun delete(tenantId: Int)

    @Upsert
    abstract fun store(vararg groups: GroupModel)
}