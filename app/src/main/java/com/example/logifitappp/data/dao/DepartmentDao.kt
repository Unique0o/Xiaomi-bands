package com.example.logifitappp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.logifitappp.data.models.DepartmentModel

@Dao
abstract class DepartmentDao {
    @Query("SELECT * FROM departments WHERE country_id = :countryId")
    abstract fun all(countryId: Int): List<DepartmentModel>

    @Query("DELETE FROM departments")
    abstract fun delete()

    @Transaction
    open suspend fun replaceAll(vararg departments: DepartmentModel) {
        delete()
        store(*departments)
    }

    @Upsert
    abstract fun store(vararg departments: DepartmentModel)
}