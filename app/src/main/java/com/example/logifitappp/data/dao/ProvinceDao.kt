package com.example.logifitappp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.logifitappp.data.models.ProvinceModel

@Dao
abstract class ProvinceDao {
    @Query("SELECT * FROM users WHERE department_id = :departmentExternalIdentifier")
    abstract fun getProvincesByDepartment(departmentExternalIdentifier: Int): List<ProvinceModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertProvinces(provinces: List<ProvinceModel>)

    @Query("DELETE FROM users")
    abstract fun clearAllProvinces()
}