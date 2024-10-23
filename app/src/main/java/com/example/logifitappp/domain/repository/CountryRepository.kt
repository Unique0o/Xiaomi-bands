package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.models.CountryModel

interface CountryRepository {

    suspend fun all(): List<CountryModel>

    suspend fun replaceAll(payloads: List<CountryModel>)
}