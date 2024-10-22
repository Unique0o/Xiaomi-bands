package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.models.ProvinceModel

interface ProvinceRepository {
    suspend fun all(departmentExternalIdentifier: Int): List<ProvinceModel>
    suspend fun replaceAll(payloads: List<ProvinceModel>)
}