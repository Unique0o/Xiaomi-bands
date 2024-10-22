package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.models.DepartmentModel

interface DepartmentRepository {
    suspend fun all(countryExternalIdentifier: Int): List<DepartmentModel>
    suspend fun replaceAll(payloads: List<DepartmentModel>)
}