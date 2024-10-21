package com.example.logifitappp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.logifitappp.data.models.DepartmentModel

@Dao
abstract class DepartmentDao {
    @Query("SELECT * FROM users WHERE department_id ")
    abstract fun getAllDepartmentsByCountry(countryExternalIdentifier: Int): List<DepartmentModel>

    @Query("DELETE FROM users WHERE department_id ")
    abstract fun clearAllDepartments()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertDepartments(departments: List<DepartmentModel>)
}