package com.example.logifitappp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.logifitappp.data.models.CountryModel

@Dao
abstract class CountryDao {
    @Query("SELECT * FROM users WHERE country_id ")
    abstract fun getAllCountries(): List<CountryModel>

    @Query("DELETE FROM users")
    abstract fun clearAllCountries()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCountries(countries: List<CountryModel>)
}