package com.example.logifitappp.data.repository

import com.example.logifitappp.data.dao.CountryDao
import com.example.logifitappp.data.models.CountryModel

class CountryRepositoryImpl (private val countryDao: CountryDao) {

    suspend fun all(): List<CountryModel> {
        return countryDao.getAllCountries()
    }

    suspend fun replaceAll(payloads: List<CountryModel>) {
        countryDao.clearAllCountries()
        countryDao.insertCountries(payloads)
    }
}