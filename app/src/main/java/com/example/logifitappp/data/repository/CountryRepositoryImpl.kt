package com.example.logifitappp.data.repository

import com.example.logifitappp.data.dao.CountryDao
import com.example.logifitappp.data.models.CountryModel
import com.example.logifitappp.domain.repository.CountryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(
    private val countryDao: CountryDao
) : CountryRepository {
    override suspend fun all(): List<CountryModel> = withContext(Dispatchers.IO) {
        countryDao.getAllCountries()
    }

    override suspend fun replaceAll(payloads: List<CountryModel>) = withContext(Dispatchers.IO) {
        countryDao.replaceAll(payloads)
    }
}