package com.example.logifitappp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.logifitappp.data.models.ProvinceModel

@Dao
abstract class ProvinceDao {
    @Query("SELECT * FROM provinces WHERE department_id = :departmentId")
    abstract fun all(departmentId: Int): List<ProvinceModel>

    @Query("DELETE FROM provinces")
    abstract fun delete()

    @Transaction
    open suspend fun replaceAll(vararg provinces: ProvinceModel) {
        delete()
        store(*provinces)
    }

    @Upsert
    abstract fun store(vararg provinces: ProvinceModel)
}