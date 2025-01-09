package com.example.logifitappp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.logifitappp.data.models.CountryModel

@Dao
abstract class CountryDao {
    @Query("SELECT * FROM countries")
    abstract fun all(): List<CountryModel>

    @Query("DELETE FROM countries")
    abstract fun delete()

    @Transaction
    open suspend fun replaceAll(vararg countries: CountryModel) {
        delete()
        store(*countries)
    }

    @Upsert
    abstract fun store(vararg countries: CountryModel)
}
