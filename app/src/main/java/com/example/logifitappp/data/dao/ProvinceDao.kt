package com.example.logifitappp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.logifitappp.data.models.ProvinceModel

@Dao
abstract class ProvinceDao {
    @Query("SELECT * FROM provinces WHERE department_external_identifier = :departmentExternalIdentifier")
    abstract fun getProvincesByDepartment(departmentExternalIdentifier: Int): List<ProvinceModel>

    @Query("DELETE FROM provinces")
    abstract fun clearAllProvinces()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertProvinces(provinces: List<ProvinceModel>)

    @Transaction
    open suspend fun replaceAll(provinces: List<ProvinceModel>) {
        clearAllProvinces()
        insertProvinces(provinces)
    }
}