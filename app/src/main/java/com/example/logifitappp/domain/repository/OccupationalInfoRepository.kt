package com.example.logifitappp.domain.repository

import com.example.logifitappp.data.models.OccupationalInfoItemModel

interface OccupationalInfoRepository {
    suspend fun getOccupationalInfo(): List<OccupationalInfoItemModel>
}