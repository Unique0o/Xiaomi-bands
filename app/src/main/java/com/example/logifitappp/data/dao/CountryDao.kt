package com.example.logifitappp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.logifitappp.data.models.CountryModel

@Dao
abstract class CountryDao {
    @Query("SELECT * FROM countries")
    abstract fun getAllCountries(): List<CountryModel>

    @Query("DELETE FROM countries")
    abstract fun clearAllCountries()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCountries(countries: List<CountryModel>)

    @Transaction
    open suspend fun replaceAll(countries: List<CountryModel>) {
        clearAllCountries()
        insertCountries(countries)
    }
}
