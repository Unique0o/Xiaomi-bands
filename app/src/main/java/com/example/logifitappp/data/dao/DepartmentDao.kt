package com.example.logifitappp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.logifitappp.data.models.DepartmentModel

@Dao
abstract class DepartmentDao {
    @Query("SELECT * FROM departments WHERE country_external_identifier = :countryExternalIdentifier")
    abstract fun getAllDepartmentsByCountry(countryExternalIdentifier: Int): List<DepartmentModel>

    @Query("DELETE FROM departments")
    abstract fun clearAllDepartments()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertDepartments(departments: List<DepartmentModel>)

    @Transaction
    open suspend fun replaceAll(departments: List<DepartmentModel>) {
        clearAllDepartments()
        insertDepartments(departments)
    }
}